package util.hash;

import me.gv7.woodpecker.tools.codec.HexUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Digest {
    private byte[] digest;

    public Digest(String algorithm,byte[] message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(message);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] toBytes(){
        return digest;
    }

    public String toBase64(){
        return Base64.getEncoder().encodeToString(digest);
    }

    public String toHex(){
        return HexUtil.encode(digest);
    }
}