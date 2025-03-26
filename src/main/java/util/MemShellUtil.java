package util;

import jmg.core.config.AbstractConfig;
import jmg.core.config.Constants;
import jmg.core.generator.InjectorGenerator;
import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IPluginHelper;
import me.gv7.woodpecker.tools.misc.RandomUtil;
import util.jmg.ShellGeneratorUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MemShellUtil {
    public static List<IArg> generateMemShellArgs(IPluginHelper helper){
        IArg tool = helper.createArg();
        tool.setName("tool");
        tool.setRequired(true);
        tool.setDefaultValue(Constants.TOOL_GODZILLA);
        tool.setDescription("WebShell类型");
        tool.setType(IArg.ARG_TYPE_ENUM);
        tool.setEnumValue(Arrays.asList(Constants.TOOL_GODZILLA, Constants.TOOL_ANTSWORD,
                Constants.TOOL_BEHINDER, Constants.TOOL_NEOREGEORG, Constants.TOOL_SUO5,
                Constants.TOOL_CUSTOM));

        IArg serverType = helper.createArg();
        serverType.setName("server-type");
        serverType.setRequired(true);
        serverType.setDefaultValue(Constants.SERVER_TOMCAT);
        serverType.setDescription("中间件类型");
        serverType.setType(IArg.ARG_TYPE_ENUM);
        serverType.setEnumValue(Arrays.asList(
                Constants.SERVER_TOMCAT,
                Constants.SERVER_SPRING_MVC,
                Constants.SERVER_SPRING_WEBFLUX,
                Constants.SERVER_JETTY,
                Constants.SERVER_RESIN,
                Constants.SERVER_WEBLOGIC,
                Constants.SERVER_WEBSPHERE,
                Constants.SERVER_UNDERTOW,
                Constants.SERVER_GLASSFISH,
                Constants.SERVER_JBOSS));

        IArg shellType = helper.createArg();
        shellType.setName("shell-type");
        shellType.setRequired(true);
        shellType.setType(IArg.ARG_TYPE_ENUM);
        shellType.setEnumValue(Arrays.asList(
                Constants.SHELL_LISTENER,
                Constants.SHELL_FILTER,
                Constants.SHELL_VALVE,
                Constants.SHELL_INTERCEPTOR,
                Constants.SHELL_WF_HANDLERMETHOD,
                Constants.SHELL_WS_ENDPOINT));
        shellType.setDefaultValue(Constants.SHELL_FILTER);
        shellType.setDescription("内存马类型");

        IArg pass = helper.createArg();
        pass.setName("pass");
        pass.setDefaultValue("pass");
        pass.setDescription("密码");
        pass.setRequired(true);

        IArg key = helper.createArg();
        key.setName("key");
        key.setDefaultValue("key");
        key.setDescription("密钥");
        key.setRequired(true);

        IArg shellClassName = helper.createArg();
        shellClassName.setName("shell-classname");
        shellClassName.setDescription("shell类名");
        shellClassName.setDefaultValue(RandomUtil.getRandomString(5));
        shellClassName.setRequired(true);

        IArg injectorClassName = helper.createArg();
        injectorClassName.setName("injector-classname");
        injectorClassName.setDescription("注入器类名");
        injectorClassName.setDefaultValue(RandomUtil.getRandomString(5));
        injectorClassName.setRequired(true);

        IArg urlPattern = helper.createArg();
        urlPattern.setName("url-pattern");
        urlPattern.setDescription("内存马路径");
        urlPattern.setDefaultValue("/*");
        urlPattern.setRequired(true);

        IArg headerKey = helper.createArg();
        headerKey.setName("header-key");
        headerKey.setDescription("请求键");
        headerKey.setDefaultValue(RandomUtil.getRandomString(5));
        headerKey.setRequired(true);

        IArg headerValue = helper.createArg();
        headerValue.setName("header-value");
        headerValue.setDescription("请求值");
        headerValue.setDefaultValue(RandomUtil.getRandomString(5));
        headerValue.setRequired(true);

        return Arrays.asList(tool,serverType,shellType,pass,key,shellClassName,injectorClassName,urlPattern,headerKey,headerValue);
    }

    public static AbstractConfig initShellConfig(Map params){
        try {
            String toolType = (String) params.get("tool");
            String serverType = (String)params.get("server-type");
            String shellType = (String)params.get("shell-type");
            String pass = (String)params.get("pass");
            String key = (String)params.get("key");
            String shellClassName = (String)params.get("shell-classname");
            String injectorClassName = (String)params.get("injector-classname");
            String urlPattern = (String)params.get("url-pattern");
            String headerKey =(String) params.get("header-key");
            String headerValue = (String)params.get("header-value");

            AbstractConfig config = new AbstractConfig();
            config.setToolType(toolType);
            config.setServerType(serverType);
            config.setShellType(shellType);
            config.setPass(pass);
            config.setKey(key);
            config.setShellClassName(shellClassName);
            config.setInjectorClassName(injectorClassName);
            config.setUrlPattern(urlPattern);
            config.setHeaderName(headerKey);
            config.setHeaderValue(headerValue);
            new ShellGeneratorUtil().makeShell(config);
            new InjectorGenerator().makeInjector(config);
            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
