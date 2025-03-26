package plugins.vul.xxljob;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

import java.util.ArrayList;

public class Hessian_RPC_Deserialization_Plugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;
    @Override
    public void VulPluginMain(IVulPluginCallbacks pluginCallbacks) {
        vulPluginCallbacks = pluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("xxl-job rpc hessian反序列化漏洞插件");
        vulPluginCallbacks.setVulName("xxl-job rpc hessian反序列化");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("xxl-job");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.setVulId("");
        ArrayList<IExploit> exploits = new ArrayList<>();
        exploits.add(new Hessian_RPC_Deserialization_JNDI_Exploit());
        exploits.add(new Hessian_RPC_Deserialization_XSLT_ReverseShell_Exploit());
        vulPluginCallbacks.registerExploit(exploits);
    }
}
