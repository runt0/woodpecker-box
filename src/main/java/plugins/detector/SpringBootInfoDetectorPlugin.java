package plugins.detector;

import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.InfoDetector;
import me.gv7.woodpecker.plugin.InfoDetectorPlugin;
import me.gv7.woodpecker.plugin.InfoDetectorPluginCallbacks;

import java.util.ArrayList;
import java.util.List;

public class SpringBootInfoDetectorPlugin implements InfoDetectorPlugin {
    public static InfoDetectorPluginCallbacks infoDetecPluginCallbacks;
    public static IPluginHelper pluginHelper;

    @Override
    public void InfoDetectorPluginMain(InfoDetectorPluginCallbacks infoDetectorPluginCallbacks) {
        this.infoDetecPluginCallbacks = infoDetectorPluginCallbacks;
        this.pluginHelper = infoDetecPluginCallbacks.getPluginHelper();
        this.infoDetecPluginCallbacks.setInfoDetectorPluginName("springboot infodetector");
        this.infoDetecPluginCallbacks.setInfoDetectorPluginAuthor("runt0");
        this.infoDetecPluginCallbacks.setInfoDetectorPluginVersion("0.0.1");
        this.infoDetecPluginCallbacks.setInfoDetectorPluginDescription("springboot攻击面信息探测");
        List<InfoDetector> infoDetecs = new ArrayList<InfoDetector>();
        infoDetecs.add(new SpringbootActuatorInfoDetector());
        this.infoDetecPluginCallbacks.registerInfoDetector(infoDetecs);
    }
}
