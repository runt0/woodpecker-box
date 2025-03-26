package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
    /**
     * @description: 根据resource资源名称(相对路径)获取资源字节流
     * @author: fa1ntStar
     * @date: 2022/9/3 19:29
     *
     * @param: resourceName
     * @return: byte[]
     */
    public static byte[] getResourceBytes(String resourceName){
        try (
                InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(resourceName);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ) {
            byte[] buffer = new byte[4096];
            int len = 0 ;
            while ((len = inputStream.read(buffer))!=-1){
                bos.write(buffer,0,len);
            }
            bos.flush();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }


    public static String getResourceString(String resourceName){
        try (
                InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(resourceName);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ) {
            byte[] buffer = new byte[4096];
            int len = 0 ;
            while ((len = inputStream.read(buffer))!=-1){
                bos.write(buffer,0,len);
            }
            bos.flush();
            bos.close();
            return new String(bos.toByteArray());
        } catch (IOException e) {
            return "";
        }
    }
}
