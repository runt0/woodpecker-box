package me.gv7.woodpecker.plugin;

import plugins.detector.SpringBootInfoDetectorPlugin;
import plugins.detector.landray.LandrayOAInfoDetectorPlugin;
import plugins.helper.dahua.DahuaPasswordDecryptPlugin;
import plugins.helper.finereport.FineReportDecoderPlugin;
import plugins.helper.kingdee.KingdeeDecoderPlugin;
import plugins.helper.spring.SpringBeanPlugin;
import plugins.helper.trick.TrickyJSPGeneratorPlugin;
import plugins.vul.activemq.Activemq_OpenWire_Deserialization_Plugin;
import plugins.vul.finereport.FineReportChannelDeserializationPlugin;
import plugins.vul.geoserver.GeoServer_GetPropertyValue_Plugin;
import plugins.vul.jeecgboot.Jeecgboot_JDBC_RCE_VulPlugin;
import plugins.vul.raqsoft.RaqsoftReportCenter_DataSphereServlet_ArbitraryFileUpload_Plugin;
import plugins.vul.raqsoft.RaqsoftReportCenter_DqlServlet_ArbitraryFileUpload_Plugin;
import plugins.vul.spring.cloud.gateway.CVE_2022_22947_Plugin;
import plugins.vul.spring.web.SpringWebRPC_HessianDeserialize_Plugin;
import plugins.vul.xxljob.Hessian_RPC_Deserialization_Plugin;

public class WoodpeckerPluginManager implements IPluginManager {
    @Override
    public void registerPluginManagerCallbacks(IPluginManagerCallbacks iPluginManagerCallbacks) {
        // info detector plugin
        iPluginManagerCallbacks.registerInfoDetectorPlugin(new SpringBootInfoDetectorPlugin());
        iPluginManagerCallbacks.registerInfoDetectorPlugin(new LandrayOAInfoDetectorPlugin());

        // vul plugin
        iPluginManagerCallbacks.registerVulPlugin(new CVE_2022_22947_Plugin());
        iPluginManagerCallbacks.registerVulPlugin(new FineReportChannelDeserializationPlugin());
        iPluginManagerCallbacks.registerVulPlugin(new Hessian_RPC_Deserialization_Plugin());
        iPluginManagerCallbacks.registerVulPlugin(new SpringWebRPC_HessianDeserialize_Plugin());
        iPluginManagerCallbacks.registerVulPlugin(new RaqsoftReportCenter_DataSphereServlet_ArbitraryFileUpload_Plugin());
        iPluginManagerCallbacks.registerVulPlugin(new RaqsoftReportCenter_DqlServlet_ArbitraryFileUpload_Plugin());
        iPluginManagerCallbacks.registerVulPlugin(new Jeecgboot_JDBC_RCE_VulPlugin());
        iPluginManagerCallbacks.registerVulPlugin(new GeoServer_GetPropertyValue_Plugin());
        iPluginManagerCallbacks.registerVulPlugin(new Activemq_OpenWire_Deserialization_Plugin());

        // helper plugin
        iPluginManagerCallbacks.registerHelperPlugin(new KingdeeDecoderPlugin());
        iPluginManagerCallbacks.registerHelperPlugin(new TrickyJSPGeneratorPlugin());
        iPluginManagerCallbacks.registerHelperPlugin(new FineReportDecoderPlugin());
        iPluginManagerCallbacks.registerHelperPlugin(new DahuaPasswordDecryptPlugin());
        iPluginManagerCallbacks.registerHelperPlugin(new SpringBeanPlugin());
    }
}
