package plugins.vul.finereport;

import me.gv7.woodpecker.plugin.IExploit;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.IVulPlugin;
import me.gv7.woodpecker.plugin.IVulPluginCallbacks;

import java.util.Collections;

public class FineReportChannelDeserializationPlugin implements IVulPlugin {
    public static IVulPluginCallbacks vulPluginCallbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void VulPluginMain(IVulPluginCallbacks iVulPluginCallbacks) {
        vulPluginCallbacks = iVulPluginCallbacks;
        pluginHelper = vulPluginCallbacks.getPluginHelper();

        vulPluginCallbacks.setVulPluginName("帆软Channel接口反序列化漏洞");
        vulPluginCallbacks.setVulName("帆软Channel接口反序列化漏洞");
        vulPluginCallbacks.setVulPluginAuthor("runt0");
        vulPluginCallbacks.setVulPluginVersion("0.0.1");
        vulPluginCallbacks.setVulProduct("FineReport10.0/11.0、FineBI5.1");
        vulPluginCallbacks.setVulSeverity(IVulPluginCallbacks.VUL_CATEGORY_RCE);
        vulPluginCallbacks.setVulId("");
        vulPluginCallbacks.registerExploit(Collections.<IExploit>singletonList(new Channel_Deserialization_Exploit()));

    }
}
