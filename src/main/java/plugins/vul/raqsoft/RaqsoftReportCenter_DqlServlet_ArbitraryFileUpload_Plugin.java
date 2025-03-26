package plugins.vul.raqsoft;

import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

public class RaqsoftReportCenter_DqlServlet_ArbitraryFileUpload_Plugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void VulPluginMain(IVulPluginCallbacks iVulPluginCallbacks) {
        vulPluginCallbacks = iVulPluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("润乾报表DqlServlet接口任意文件上传插件");
        vulPluginCallbacks.setVulName("润乾报表DqlServlet接口任意文件上传漏洞");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("润乾报表中心");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.registerPoc(new DqlServlet_ArbitraryFileUpload_POC());
    }
}
