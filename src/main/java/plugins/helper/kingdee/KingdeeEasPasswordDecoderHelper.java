package plugins.helper.kingdee;

import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;
import plugins.helper.trick.TrickyJSPGeneratorPlugin;
import util.kingdee.Rijndael_Algorithm;
import util.kingdee.XUtil;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class KingdeeEasPasswordDecoderHelper implements IHelper {
    @Override
    public String getHelperTabCaption() {
        return "Kingdee EAS Password Decoder";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = TrickyJSPGeneratorPlugin.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<>();
        //是否压缩
        IArg passwordArg = TrickyJSPGeneratorPlugin.pluginHelper.createArg();
        passwordArg.setName("password");
        passwordArg.setDescription("金蝶EAS加密后的数据库密码");
        passwordArg.setRequired(true);
        args.add(passwordArg);
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String password = map.get("password").toString();
        if (password.startsWith("ksqle")){
            password = password.substring("ksqle:".length());
            String plainText = kingdeeDecrypt(password);
            iResultOutput.successPrintln(plainText);
        }else {
            iResultOutput.errorPrintln("password should be started with \"ksqle\"");
        }
    }

    public String kingdeeDecrypt(String cipher){
        byte[] pt;
        try {
            byte[] kb = XUtil.hex2byte(XUtil.xDecode(new String(Base64.getDecoder().decode("VUVvSjFBWUF0RjFaMEYyQXBFb0IyRllCMEVvSjBGWTMyRW5aMEJIRW1CMTYr"))));
            byte[] ct = Base64.getDecoder().decode(cipher);
            int blockSize = 16;
            int mode = ct.length % blockSize;
            Object key = Rijndael_Algorithm.makeKey(kb, blockSize);
            int i;
            byte[] block_pt;
            if (mode == 0) {
                pt = new byte[ct.length];

                for(i = 0; i < ct.length; i += blockSize) {
                    block_pt = Rijndael_Algorithm.blockDecrypt(ct, i, key, blockSize);
                    System.arraycopy(block_pt, 0, pt, i, blockSize);
                }
            } else {
                int dataLength;
                Object first_key;
                if (blockSize != 16 && blockSize != 24) {
                    first_key = Rijndael_Algorithm.makeKey(kb, 24);
                    block_pt = Rijndael_Algorithm.blockDecrypt(ct, ct.length - 24, first_key, 24);
                    dataLength = getInt(block_pt, 16);

                    pt = new byte[dataLength];

                    if (dataLength > ct.length - 24) {
                        System.arraycopy(block_pt, 0, pt, ct.length - 24, dataLength - (ct.length - 24));
                    }

                    Object key_16 = Rijndael_Algorithm.makeKey(kb, 16);
                    byte[] last_block_16_pt = Rijndael_Algorithm.blockDecrypt(ct, ct.length - 24 - 16, key_16, 16);
                    if (pt.length > ct.length - 24) {
                        System.arraycopy(last_block_16_pt, 0, pt, ct.length - 24 - 16, 16);
                    } else {
                        System.arraycopy(last_block_16_pt, 0, pt, ct.length - 24 - 16, pt.length - (ct.length - 24 - 16));
                    }
                } else {
                    first_key = Rijndael_Algorithm.makeKey(kb, blockSize + 8);
                    block_pt = Rijndael_Algorithm.blockDecrypt(ct, ct.length - blockSize - 8, first_key, blockSize + 8);
                    dataLength = getInt(block_pt, blockSize);
                    pt = new byte[dataLength];
                    System.arraycopy(block_pt, 0, pt, ct.length - blockSize - 8, dataLength % blockSize);
                }

                for(i = 0; i < ct.length - blockSize - 8; i += blockSize) {
                    block_pt = Rijndael_Algorithm.blockDecrypt(ct, i, key, blockSize);
                    System.arraycopy(block_pt, 0, pt, i, blockSize);
                }
            }
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return new String(pt);
    }

    public int getInt(byte[] bytes, int offSet) {
        return (bytes[offSet + 0] & 255) << 24 | bytes[offSet + 1] << 16 | (bytes[offSet + 2] & 255) << 8 | (bytes[offSet + 3] & 255) << 0;
    }
}
