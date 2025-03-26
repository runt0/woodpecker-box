package plugins.helper.dahua;

import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;
import me.gv7.woodpecker.tools.codec.HexUtil;
import plugins.helper.trick.TrickyJSPGeneratorPlugin;
import util.hash.Hash;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class DahuaICCPasswordDecryptHelper implements IHelper {

    @Override
    public String getHelperTabCaption() {
        return "properties文件数据库密码解密";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = TrickyJSPGeneratorPlugin.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<>();

        IArg passwordArg = DahuaPasswordDecryptPlugin.pluginHelper.createArg();
        passwordArg.setName("password");
        passwordArg.setDescription("properties文件中加密的密码字段");
        passwordArg.setRequired(true);
        args.add(passwordArg);
        argsUsageBinder.setArgsList(args);

        IArg seedSaveText = DahuaPasswordDecryptPlugin.pluginHelper.createArg();
        seedSaveText.setName("seed-save-text");
        seedSaveText.setDescription("算法必须值,不确定每个部署环境是否相同,参考存放位置(/opt/evo/ssl/MV3G6LLFNFTWK3TWMFWHKZI.key)");
        seedSaveText.setDefaultValue("AIOaOUZ1RUd4RalxSER4O0WGPUR1NUSGNamDOlV5PUV0RlSmen8=");
        seedSaveText.setRequired(true);
        args.add(seedSaveText);

        argsUsageBinder.setArgsList(args);

        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String password = (String) map.get("password");
        String seedSaveText = (String) map.get("seed-save-text");
        String seed = getDSeed2(seedSaveText);
        seed = new String(java.util.Base64.getDecoder().decode(seed));
        seed = seed.substring(3, seed.length() - 3);
        seed = Hash.md5(seed).toHex();
        seed = Hash.sha256(seed + "dh-dss-evo-7ujMko0").toHex();
        seed = Base64.getEncoder().encodeToString(seed.getBytes());
        seed = Hash.md5(seed).toHex().toUpperCase();

        String ivS = password.substring(0, 32);
        String data = password.substring(32, password.length());

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed.getBytes());
        kgen.init(256, sr);
        SecretKey skey = kgen.generateKey();
        byte[] key = skey.getEncoded();
        byte[] cipherBytes = HexUtil.decode(data);
        byte[] iv = HexUtil.decode(ivS);

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        cipher.init(2, (Key)skeySpec, zeroIv);
        byte[] plainText = cipher.doFinal(cipherBytes);
        iResultOutput.successPrintln(new String(plainText));
    }

    public static String getDSeed2(String seed3) throws UnsupportedEncodingException {
        byte[] seed3s = seed3.getBytes("UTF-8");
        byte[] seed2s = new byte[seed3s.length];
        int i = 0;
        byte[] byArray = seed3s;
        int n = seed3s.length;
        int n2 = 0;
        while (n2 < n) {
            int sb = byArray[n2];
            seed2s[i] = (byte) (65 < sb && sb <= 90 || 97 < sb && sb <= 122 ? (sb = (byte)(sb - 1)) : (sb == 65 ? 90 : (sb == 97 ? 122 : sb)));
            ++i;
            ++n2;
        }
        return new String(seed2s, "UTF-8");
    }
}
