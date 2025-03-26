package plugins.helper.misc;

import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IHelperPlugin;
import me.gv7.woodpecker.plugin.IHelperPluginCallbacks;
import me.gv7.woodpecker.plugin.IPluginHelper;
import plugins.helper.kingdee.KingdeeEasPasswordDecoderHelper;

import java.util.ArrayList;
import java.util.List;

public class GodzillaFlowHelperPlugin implements IHelperPlugin {
    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;
    @Override
    public void HelperPluginMain(IHelperPluginCallbacks iHelperPluginCallbacks) {
        this.callbacks = iHelperPluginCallbacks;
        this.pluginHelper = iHelperPluginCallbacks.getPluginHelper();
        callbacks.setHelperPluginName("godzilla flow decrypt");
        callbacks.setHelperPluginVersion("0.0.1");
        callbacks.setHelperPluginAutor("runt0");
        callbacks.setHelperPluginDescription("哥斯拉流量处理插件");
        List<IHelper> helperList = new ArrayList<IHelper>();
        helperList.add(new BigFileUploadExtractHelperPlugin());
        callbacks.registerHelper(helperList);
    }
}
