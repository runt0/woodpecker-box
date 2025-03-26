package util.jmg;

import jmg.core.config.AbstractConfig;
import jmg.core.config.Constants;
import me.gv7.woodpecker.plugin.IResultOutput;

public class ResultUtil {

    public static void printAntSwordBasicInfo(AbstractConfig config, IResultOutput output) {
        output.infoPrintln("基础信息:");
        
        output.infoPrintln("密码: " + config.getPass());
        output.infoPrintln("请求路径: " + config.getUrlPattern());
        output.infoPrintln("请求头: " + config.getHeaderName() + ": " + config.getHeaderValue());
        output.infoPrintln("脚本类型: JSP");
        
    }

    public static void printGodzillaBasicInfo(AbstractConfig config, IResultOutput output) {
        switch (config.getShellType()) {
            case Constants.SHELL_LISTENER:
            case Constants.SHELL_FILTER:
            case Constants.SHELL_INTERCEPTOR:
                output.successPrintln("基础信息:");
                
                output.infoPrintln("加密器: JAVA_AES_BASE64");
                output.infoPrintln("密码: " + config.getPass());
                output.infoPrintln("密钥: " + config.getKey());
                output.infoPrintln("请求路径: " + config.getUrlPattern());
                output.infoPrintln("请求头: " + config.getHeaderName() + ": " + config.getHeaderValue());
                output.infoPrintln("脚本类型: JSP");
                break;
            case Constants.SHELL_WF_HANDLERMETHOD:
                output.successPrintln("基础信息:");
                output.infoPrintln("加密器: JAVA_AES_BASE64");
                output.infoPrintln("密码: " + config.getPass());
                output.infoPrintln("密钥: " + config.getKey());
                output.infoPrintln("请求路径: " + config.getUrlPattern());
                break;
        }
    }

    public static void printBehinderBasicInfo(AbstractConfig config,IResultOutput output) {
        output.successPrintln("基础信息:");
        output.infoPrintln("密码: " + config.getPass());
        output.infoPrintln("请求路径: " + config.getUrlPattern());
        output.infoPrintln("请求头: " + config.getHeaderName() + ": " + config.getHeaderValue());
        output.infoPrintln("脚本类型: JSP");
        output.infoPrintln("内存马类名: " + config.getShellClassName());
        output.infoPrintln("注入器类名: " + config.getInjectorClassName());

    }

    public static void printSuo5BasicInfo(AbstractConfig config,IResultOutput output) {
        output.successPrintln("基础信息:");
        output.infoPrintln("请求路径: " + config.getUrlPattern());
        output.infoPrintln("连接指令:");
        if (config.getHeaderName().equalsIgnoreCase("user-agent")) {
            output.infoPrintln(String.format("     ./suo5 -d --ua '%s' -t http://", config.getHeaderValue()));
            output.infoPrintln(String.format("     ./suo5 -d -l 0.0.0.0:7788 --auth test:test123 --ua '%s' -t http://", config.getHeaderValue()));
        } else {
            output.infoPrintln(String.format("     ./suo5 -H '%s: %s' -t http://", config.getHeaderName(), config.getHeaderValue()));
            output.infoPrintln(String.format("     ./suo5 -l 0.0.0.0:7788 --auth test:test123 -H '%s: %s' -t http://", config.getHeaderName(), config.getHeaderValue()));
        }
        
    }

    public static void printNeoreGeorgBasicInfo(AbstractConfig config,IResultOutput output) {
        output.successPrintln("基础信息:");
        output.infoPrintln("密钥: " + config.getKey());
        output.infoPrintln("请求路径: " + config.getUrlPattern());
        output.infoPrintln("连接指令:");
        output.infoPrintln(String.format("     python3 neoreg.py -k %s -H '%s:%s' -u http://", config.getKey(), config.getHeaderName(), config.getHeaderValue()));
        output.infoPrintln(String.format("     python3 neoreg.py --skip --proxy http://127.0.0.1:8080 -vv -k %s -H '%s:%s' -u http:// ", config.getKey(), config.getHeaderName(), config.getHeaderValue()));
    }


    public static void resultOutput(AbstractConfig config,IResultOutput output) throws Throwable {
        switch (config.getToolType()) {
            case Constants.TOOL_ANTSWORD:
                printAntSwordBasicInfo(config,output);
                break;
            case Constants.TOOL_BEHINDER:
                printBehinderBasicInfo(config,output);
                break;
            case Constants.TOOL_GODZILLA:
                printGodzillaBasicInfo(config,output);
                break;
            case Constants.TOOL_SUO5:
                printSuo5BasicInfo(config,output);
                break;
            case Constants.TOOL_NEOREGEORG:
                printNeoreGeorgBasicInfo(config,output);
                break;
        }

    }

}
