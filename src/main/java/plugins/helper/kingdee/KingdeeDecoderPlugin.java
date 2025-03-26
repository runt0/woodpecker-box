package plugins.helper.kingdee;

import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IHelperPlugin;
import me.gv7.woodpecker.plugin.IHelperPluginCallbacks;
import me.gv7.woodpecker.plugin.IPluginHelper;

import java.util.ArrayList;
import java.util.List;

public class KingdeeDecoderPlugin implements IHelperPlugin {
    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void HelperPluginMain(IHelperPluginCallbacks pluginCallbacks) {
        this.callbacks = pluginCallbacks;
        this.pluginHelper = pluginCallbacks.getPluginHelper();
        callbacks.setHelperPluginName("kingdee password decoder");
        callbacks.setHelperPluginVersion("0.0.1");
        callbacks.setHelperPluginAutor("runt0");
        callbacks.setHelperPluginDescription("金蝶相关产品密码解码");
        List<IHelper> helperList = new ArrayList<IHelper>();
        helperList.add(new KingdeeEasPasswordDecoderHelper());
        callbacks.registerHelper(helperList);
    }
}
