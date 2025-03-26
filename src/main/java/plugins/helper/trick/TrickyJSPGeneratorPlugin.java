package plugins.helper.trick;

import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IHelperPlugin;
import me.gv7.woodpecker.plugin.IHelperPluginCallbacks;
import me.gv7.woodpecker.plugin.IPluginHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class TrickyJSPGeneratorPlugin implements IHelperPlugin {

    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void HelperPluginMain(IHelperPluginCallbacks pluginCallbacks) {
        this.callbacks = pluginCallbacks;
        this.pluginHelper = pluginCallbacks.getPluginHelper();
        callbacks.setHelperPluginName("Tricky JSP Generator");
        callbacks.setHelperPluginVersion("0.0.1");
        callbacks.setHelperPluginAutor("runt0");
        callbacks.setHelperPluginDescription("常用JSP生成及一些简单的变形, 用于绕过部分流量检测和主机检测");
        List<IHelper> helperList = new ArrayList<IHelper>();
        helperList.add(new BehinderWebShellGenerateHelper());
        helperList.add(new Suo5JSPGenerateHelper());
        callbacks.registerHelper(helperList);
    }
}
