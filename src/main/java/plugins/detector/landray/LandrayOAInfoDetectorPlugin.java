package plugins.detector.landray;

import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.plugin.InfoDetector;
import me.gv7.woodpecker.plugin.InfoDetectorPlugin;
import me.gv7.woodpecker.plugin.InfoDetectorPluginCallbacks;

import java.util.ArrayList;
import java.util.List;

public class LandrayOAInfoDetectorPlugin implements InfoDetectorPlugin {
    public static InfoDetectorPluginCallbacks callbacks;
    public static IPluginHelper helper;


    @Override
    public void InfoDetectorPluginMain(InfoDetectorPluginCallbacks infoDetectorPluginCallbacks) {
        this.callbacks = infoDetectorPluginCallbacks;
        this.helper = callbacks.getPluginHelper();
        this.callbacks.setInfoDetectorPluginName("蓝凌OA攻击面探测插件");
        this.callbacks.setInfoDetectorPluginAuthor("runt0");
        this.callbacks.setInfoDetectorPluginVersion("0.0.1");
        this.callbacks.setInfoDetectorPluginDescription("蓝凌OA攻击面探测");
        List<InfoDetector> infoDetecs = new ArrayList<InfoDetector>();
        infoDetecs.add(new LandrayOAInfoDetector());
        this.callbacks.registerInfoDetector(infoDetecs);
    }
}
