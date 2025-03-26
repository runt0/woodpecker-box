package plugins.helper.spring;

import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IHelperPlugin;
import me.gv7.woodpecker.plugin.IHelperPluginCallbacks;
import me.gv7.woodpecker.plugin.IPluginHelper;

import java.util.ArrayList;
import java.util.List;

public class SpringBeanPlugin implements IHelperPlugin {
    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void HelperPluginMain(IHelperPluginCallbacks pluginCallbacks) {
        this.callbacks = pluginCallbacks;
        this.pluginHelper = pluginCallbacks.getPluginHelper();
        callbacks.setHelperPluginName("spring beans xml生成");
        callbacks.setHelperPluginVersion("0.0.1");
        callbacks.setHelperPluginAutor("runt0");
        callbacks.setHelperPluginDescription("spring beans xml生成");
        List<IHelper> helperList = new ArrayList<IHelper>();
        helperList.add(new SpringBeanXMLGeneratorHelper());
        callbacks.registerHelper(helperList);
    }
}
