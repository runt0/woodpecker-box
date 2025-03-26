package plugins.helper.misc;

import me.gv7.woodpecker.plugin.IArg;
import me.gv7.woodpecker.plugin.IArgsUsageBinder;
import me.gv7.woodpecker.plugin.IHelper;
import me.gv7.woodpecker.plugin.IResultOutput;
import me.gv7.woodpecker.tools.common.FileUtil;
import util.hash.Hash;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class BigFileUploadExtractHelperPlugin implements IHelper {
    @Override
    public String getHelperTabCaption() {
        return "大文件上传请求-文件提取";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        IArgsUsageBinder argsUsageBinder = GodzillaFlowHelperPlugin.pluginHelper.createArgsUsageBinder();
        List<IArg> args = new ArrayList<>();

        IArg requestFilePathArg = GodzillaFlowHelperPlugin.pluginHelper.createArg();
        requestFilePathArg.setName("request-file-path");
        requestFilePathArg.setDescription("加密请求数据包文件路径");
        requestFilePathArg.setRequired(true);
        args.add(requestFilePathArg);
        argsUsageBinder.setArgsList(args);

        IArg extractFilePathArg = GodzillaFlowHelperPlugin.pluginHelper.createArg();
        extractFilePathArg.setName("extract-file-path");
        extractFilePathArg.setDescription("提取文件存放路径");
        extractFilePathArg.setRequired(true);
        args.add(extractFilePathArg);
        argsUsageBinder.setArgsList(args);

        IArg keyArg = GodzillaFlowHelperPlugin.pluginHelper.createArg();
        keyArg.setName("key");
        keyArg.setDescription("Godzilla Key参数");
        keyArg.setRequired(true);
        args.add(keyArg);
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String requestFilePath = map.get("request-file-path").toString();
        String extractFilePath = map.get("extract-file-path").toString();
        if (!extractFilePath.endsWith("/")){
            extractFilePath = extractFilePath + "/";
        }
        byte[] request = FileUtil.readFile(requestFilePath);
        String key = map.get("key").toString();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(Hash.md5(key).toHex().substring(0,16).getBytes(),"AES"));
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(request));
        bos = new ByteArrayOutputStream();
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(plainText));
        int b = 0;
        byte[] data = new byte[1024];
        while ((b=gis.read(data))!=-1){
            bos.write(data,0,b);
        }
        bos.flush();
        bos.close();
        byte[] plainTextDecompress = bos.toByteArray();

        Parameter parameter = Parameter.unSerialize(plainTextDecompress);
        String landingFilename = parameter.getParameterString("fileName");
        String filename = landingFilename;
        if (landingFilename.lastIndexOf("/") > 0){
            filename = landingFilename.substring(landingFilename.lastIndexOf("/"));
        } else if (landingFilename.lastIndexOf("\\") > 0) {
            filename = landingFilename.substring(landingFilename.lastIndexOf("\\"));
        }

        iResultOutput.infoPrintln("landing filename: " + landingFilename);
        byte[] fileContents = parameter.get("fileContents");
        FileOutputStream fos = new FileOutputStream(extractFilePath + filename);
        fos.write(fileContents);
        fos.flush();
        fos.close();

        iResultOutput.successPrintln("extract file success");
    }
}
