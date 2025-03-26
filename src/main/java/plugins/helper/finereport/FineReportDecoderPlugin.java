package plugins.helper.finereport;

import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IHelperPlugin;
import me.gv7.woodpecker.plugin.IHelperPluginCallbacks;
import me.gv7.woodpecker.plugin.IPluginHelper;

import java.util.ArrayList;
import java.util.List;

public class FineReportDecoderPlugin implements IHelperPlugin {
    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;
    @Override
    public void HelperPluginMain(IHelperPluginCallbacks callbacks) {
        this.callbacks = callbacks;
        this.pluginHelper = callbacks.getPluginHelper();
        callbacks.setHelperPluginName("finereport password decoder");
        callbacks.setHelperPluginVersion("0.0.1");
        callbacks.setHelperPluginAutor("runt0");
        callbacks.setHelperPluginDescription("帆软报表数据库密码解码");
        List<IHelper> helperList = new ArrayList<IHelper>();
        helperList.add(new FineReportPasswordDecodeHelper());
        callbacks.registerHelper(helperList);
    }
}