package plugins.helper.finereport;

import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;
import plugins.helper.trick.TrickyJSPGeneratorPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FineReportPasswordDecodeHelper implements IHelper {

    @Override
    public String getHelperTabCaption() {
        return "FineReport Password Decoder";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = TrickyJSPGeneratorPlugin.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<>();
        //是否压缩
        IArg passwordArg = TrickyJSPGeneratorPlugin.pluginHelper.createArg();
        passwordArg.setName("password");
        passwordArg.setDescription("帆软加密后的数据库密码");
        passwordArg.setRequired(true);
        args.add(passwordArg);
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String password = map.get("password").toString();
        int[] maskArray = { 19, 78, 10, 15, 100, 213, 43, 23 };
        if ((password != null) && (password.startsWith("___"))) {
            password = password.substring(3);
            StringBuilder localStringBuilder = new StringBuilder();
            int i = 0;
            for (int j = 0; j <= password.length() - 4; j += 4) {
                if (i == maskArray.length) {
                    i = 0;
                }
                String str = password.substring(j, j + 4);
                int k = Integer.parseInt(str, 16) ^ maskArray[i];
                localStringBuilder.append((char)k);
                i++;
            }
            password = localStringBuilder.toString();
            iResultOutput.successPrintln(password);
        } else {
            iResultOutput.errorPrintln("密码格式错误");
        }
    }
}
