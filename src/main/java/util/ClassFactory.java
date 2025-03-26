package util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ClassFactory {
    private static ClassPool pool = ClassPool.getDefault();
    private static Class swingLazyValueClass = null;


    public static Class getSwingLazyValueClass(){
        try {
            if (swingLazyValueClass == null){
                CtClass ctClass = pool.makeClass(
                        new ByteArrayInputStream(
                                ResourceUtil.getResourceBytes("template/class/SwingLazyValue.class")
                        )
                );
                ctClass.setName("sun.swing.SwingLazyValue");
                swingLazyValueClass = ctClass.toClass();
            }
            return swingLazyValueClass;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

}
