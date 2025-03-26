package plugins.vul.spring.web;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

import java.util.Collections;

public class SpringWebRPC_HessianDeserialize_Plugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void VulPluginMain(IVulPluginCallbacks iVulPluginCallbacks) {
        vulPluginCallbacks = iVulPluginCallbacks ;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("spring web hessian rpc deserialize");
        vulPluginCallbacks.setVulName("spring web hessian rpc反序列化漏洞");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("spring web remote");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.registerExploit(Collections.<IExploit>singletonList(new SpringWebRPC_HessianDeserialize_Exploit()));
    }
}
