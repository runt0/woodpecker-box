package plugins.vul.geoserver;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

import java.util.ArrayList;

public class GeoServer_GetPropertyValue_Plugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;
    @Override
    public void VulPluginMain(IVulPluginCallbacks pluginCallbacks) {
        vulPluginCallbacks = pluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("geoserver get-property-value代码执行插件");
        vulPluginCallbacks.setVulName("geoserver get-property-value代码执行");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("geoserver");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.setVulId("CVE-2024-36401");
        ArrayList<IExploit> exploits = new ArrayList<>();
        exploits.add(new Inject_MemShell_Exploit());
        vulPluginCallbacks.registerExploit(exploits);
    }
}
