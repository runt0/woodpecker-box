package plugins.helper.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Parameter {
    protected HashMap<String, byte[]> hashMap = new HashMap<>();
    protected long size;

    public String getParameterString(String key) {
        byte[] retByteArray = getParameterByteArray(key);
        if (retByteArray != null) {
            return new String(retByteArray);
        }
        return null;
    }

    public byte[] getParameterByteArray(String key) {
        return this.hashMap.get(key);
    }

    public void addParameterString(String key, String value) {
        addParameterByteArray(key, value.getBytes());
    }

    public void addParameterByteArray(String key, byte[] value) {
        this.hashMap.put(key, value);
        this.size += (long) value.length;
    }

    public byte[] get(String key) {
        return getParameterByteArray(key);
    }

    public void add(String key, String value) {
        addParameterString(key, value);
    }

    public void add(String key, byte[] value) {
        addParameterByteArray(key, value);
    }

    public long getSize() {
        return this.size;
    }

    public static int bytesToInt(byte[] bytes) {
        return (bytes[0] & 255) | ((bytes[1] & 255) << 8) | ((bytes[2] & 255) << 16) | ((bytes[3] & 255) << 24);
    }

    public static Parameter unSerialize(byte[] parameterByte) {
        Parameter resParameter = new Parameter();
        ByteArrayInputStream tStream = new ByteArrayInputStream(parameterByte);
        ByteArrayOutputStream tp = new ByteArrayOutputStream();
        String key = null;
        byte[] lenB = new byte[4];
        Object var6 = null;

        try {
            ByteArrayInputStream inputStream = tStream;

            while(true) {
                while(true) {
                    byte t = (byte)inputStream.read();
                    if (t == -1) {
                        tp.close();
                        tStream.close();
                        inputStream.close();
                        return resParameter;
                    }

                    if (t == 2) {
                        key = tp.toString();
                        inputStream.read(lenB);
                        int len = bytesToInt(lenB);
                        byte[] data = new byte[len];
                        int readOneLen = 0;

                        while((readOneLen += inputStream.read(data, readOneLen, data.length - readOneLen)) < data.length) {
                        }

                        resParameter.addParameterByteArray(key, data);
                        tp.reset();
                    } else {
                        tp.write(t);
                    }
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
            return resParameter;
        }
    }

    public static byte[] intToBytes(int value) {
        return new byte[]{(byte) (value & 255), (byte) ((value >> 8) & 255), (byte) ((value >> 16) & 255), (byte) ((value >> 24) & 255)};
    }
    public byte[] serialize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (String key : this.hashMap.keySet()) {
            try {
                byte[] value = this.hashMap.get(key);
                outputStream.write(key.getBytes());
                outputStream.write(2);
                outputStream.write(intToBytes(value.length));
                outputStream.write(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
    }
}
