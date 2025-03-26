package plugins.helper.trick;

import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;
import util.ResourceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Suo5JSPGenerateHelper implements IHelper {
    @Override
    public String getHelperTabCaption() {
        return "Generate BCEL Encoded Suo5";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = TrickyJSPGeneratorPlugin.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<>();
        //是否压缩
        IArg compressArg = TrickyJSPGeneratorPlugin.pluginHelper.createArg();
        compressArg.setName("compress");
        compressArg.setDefaultValue("false");
        compressArg.setDescription("是否压缩(去除换行和多余的空格)");
        compressArg.setEnumValue(Arrays.asList("true","false"));
        compressArg.setRequired(true);

        //suo5类型(normal/bcel)
        IArg typeArg = TrickyJSPGeneratorPlugin.pluginHelper.createArg();
        typeArg.setName("type");
        typeArg.setDefaultValue("normal");
        typeArg.setDescription("jsp类型(normal-正常suo5jsp;bcel-bcel编码后的jsp)");
        typeArg.setEnumValue(Arrays.asList("normal","bcel"));
        typeArg.setRequired(true);

        args.add(compressArg);
        args.add(typeArg);
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String suo5 = null;
        if (map.get("type").equals("normal")){
            suo5 = new String(ResourceUtil.getResourceBytes("template/jsp/suo5.jsp"));
        } else {
            suo5 = new String(ResourceUtil.getResourceBytes("template/jsp/bcel_encoded_suo5.jsp"));
        }
        if (map.get("compress").equals("true")){
            suo5 = suo5.replace("\n","").replace("    ","");
        }
        iResultOutput.rawPrintln(suo5);
    }
}
