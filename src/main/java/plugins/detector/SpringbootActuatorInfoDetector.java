package plugins.detector;

import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IResultOutput;
import me.gv7.woodpecker.plugin.ITarget;
import me.gv7.woodpecker.plugin.InfoDetector;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpringbootActuatorInfoDetector implements InfoDetector {
    String[] exploitableEndpoints = new String[]{
            "heapdump",
            "env",
            "gateway",
            "jolokia",
            "refresh"
    };

    @Override
    public String getInfoDetectorTabCaption() {
        return "attack surface detect";
    }

    @Override
    public IArgsUsageBinder getInfoDetectorCustomArgs() {
        return null;
    }

    @Override
    public LinkedHashMap<String, String> doDetect(ITarget iTarget, Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        LinkedHashMap<String,String> informationMap = new LinkedHashMap<>();
        String baseUrl = iTarget.getAddress().endsWith("/") ?
                iTarget.getAddress().replaceAll(".$","") :
                iTarget.getAddress();

        String actuatorUrl = baseUrl + "/actuator";
        RawResponse response = Requests.get(actuatorUrl).send();
        String body = response.readToText();

        // springboot2,从response里读取
        if (response.statusCode() == 200 && response.getHeader("Content-Type").contains("json")){
            for (String endpoint:exploitableEndpoints){
                if (body.contains(endpoint)){
                    iResultOutput.successPrintln(actuatorUrl+"/"+endpoint);
                    switch (endpoint){
                        case "env":
                            break;
                        case "gateway":
                            break;
                        case "jolokia":
                            informationMap.putAll(detectJolokiaInfo(actuatorUrl+"/"+endpoint));
                            break;
                    }
                } else {
                    iResultOutput.failPrintln(actuatorUrl+"/"+endpoint);
                }
            }
        } else {
            iResultOutput.failPrintln(actuatorUrl);
            for (String endpoint:exploitableEndpoints){
                response = Requests.get(baseUrl+"/"+endpoint).send();
                body = response.readToText();
                if (body.contains("\"timestamp\":")){
                    iResultOutput.successPrintln(response.url());
                    informationMap.putAll(detectJolokiaInfo(response.url()));
                } else {
                    iResultOutput.failPrintln(response.url());
                }
            }
        }
        return informationMap;
    }

    public LinkedHashMap<String,String> detectJolokiaInfo(String url){
        LinkedHashMap<String,String> informationMap = new LinkedHashMap<>();
        RawResponse response = Requests.get(url+"/list").send();
        String body = response.readToText();
        if (body.contains("ch.qos.logback.classic.jmx.JMXConfigurator") && body.contains("reloadByURL")){
            informationMap.put("jolokia logback jndi rce","true");
        } else {
            informationMap.put("jolokia logback jndi rce","false");
        }

        if (body.contains("createJNDIRealm") && body.contains("type=MBeanFactory")){
            informationMap.put("jolokia realm jndi rce","true");
        } else {
            informationMap.put("jolokia realm jndi rce","false");
        }
        return informationMap;
    }
}
