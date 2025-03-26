package util.hash;

public class Hash {
    public static Digest md5(byte[] message){
        return new Digest("md5",message);
    }

    public static Digest md5(String message){
        return new Digest("md5",message.getBytes());
    }

    public static Digest sha256(byte[] message){
        return new Digest("sha-256",message);
    }

    public static Digest sha256(String message){
        return new Digest("sha-256",message.getBytes());
    }

}
