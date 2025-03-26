package plugins.vul.jeecgboot;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

import java.util.Collections;

public class Jeecgboot_JDBC_RCE_VulPlugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void VulPluginMain(IVulPluginCallbacks iVulPluginCallbacks) {
        vulPluginCallbacks = iVulPluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("JeecgBoot JDBC RCE");
        vulPluginCallbacks.setVulName("JeecgBoot JDBC RCE");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("JeecgBoot");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.setVulId("");
        vulPluginCallbacks.registerExploit(Collections.<IExploit>singletonList(new Jeecgboot_JDBC_RCE_Exploit()));

    }
}
