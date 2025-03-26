package plugins.vul.activemq;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

import java.util.ArrayList;

public class Activemq_OpenWire_Deserialization_Plugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;
    @Override
    public void VulPluginMain(IVulPluginCallbacks pluginCallbacks) {
        vulPluginCallbacks = pluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("apache activemq openwire反序列化远程代码执行插件");
        vulPluginCallbacks.setVulName("apache activemq openwire反序列化远程代码执行");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("apache activemq");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.setVulId("CVE-2023-46604");
        ArrayList<IExploit> exploits = new ArrayList<>();
        exploits.add(new CVE_2023_46604_Exploit());
        vulPluginCallbacks.registerExploit(exploits);
    }
}
