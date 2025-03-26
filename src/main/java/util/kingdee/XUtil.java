package util.kingdee;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class XUtil {
    private static final char[] xTable = new char[]{'+', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public XUtil() {
    }

    private static void xxd(char[] chxxe, char[] chdes) {
        int k = 2;
        chxxe[0] = (char) Arrays.binarySearch(xTable, chxxe[0]);

        for(int i = 0; i < 3; ++i) {
            chxxe[i + 1] = (char)Arrays.binarySearch(xTable, chxxe[i + 1]);
            chdes[i] = (char)(chxxe[i] << k & 257 - (1 << k));
            k += 2;
            char t = (char)(chxxe[i + 1] >> 8 - k);
            chdes[i] |= t;
        }

    }

    public static String xDecode(String src) {
        char[] out = new char[3];
        char[] tbuf = new char[4];
        char[] buf = src.toCharArray();
        int len = buf.length;
        int rlen = 0;
        StringBuffer sb = new StringBuffer();

        int fr;
        for(fr = 0; fr < len / 63; ++fr) {
            for(int j = 0; j < 15; ++j) {
                System.arraycopy(buf, fr * 63 + 1 + j * 4, tbuf, 0, 4);
                xxd(tbuf, out);
                sb.append(out);
            }

            rlen += 45;
        }

        fr = len % 63 - 1;
        rlen += Arrays.binarySearch(xTable, buf[len / 63 * 63]);
        char[] nbuf = new char[fr];
        System.arraycopy(buf, len - fr, nbuf, 0, fr);

        for(int j = 0; j < fr / 4; ++j) {
            System.arraycopy(nbuf, j * 4, tbuf, 0, 4);
            xxd(tbuf, out);
            sb.append(out);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            bos.write(charToByte(sb.toString().substring(0, rlen).toCharArray()));
            bos.close();
        } catch (IOException var11) {
        }

        return bos.toString();
    }

    private static byte[] charToByte(char[] src) {
        byte[] des = new byte[src.length];

        for(int i = 0; i < src.length; ++i) {
            if (src[i] < 128) {
                des[i] = (byte)src[i];
            } else {
                des[i] = (byte)(128 - src[i]);
            }
        }

        return des;
    }


    private static void xxe(char[] chsrc, char[] chxxe) {
        chxxe[0] = (char)(chsrc[0] >> 2);
        chxxe[1] = (char)(chsrc[0] << 4 & 48 | chsrc[1] >> 4 & 15);
        chxxe[2] = (char)(chsrc[1] << 2 & 60 | chsrc[2] >> 6 & 3);
        chxxe[3] = (char)(chsrc[2] & 63);

        for(int i = 0; i < 4; ++i) {
            chxxe[i] = xTable[chxxe[i]];
        }

    }

    private static char[] byteToChar(byte[] src) {
        char[] des = new char[src.length];

        for(int i = 0; i < src.length; ++i) {
            if (src[i] < 0) {
                des[i] = (char)(128 - src[i]);
            } else {
                des[i] = (char)src[i];
            }
        }

        return des;
    }

    public static byte[] hex2byte(String hex) {
        int length = hex.length();
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("illegal hex");
        } else {
            String[] bs = new String[length / 2];

            for(int i = 0; i < length; i += 2) {
                char[] chars = new char[2];
                hex.getChars(i, i + 2, chars, 0);
                bs[i / 2] = new String(chars);
            }

            byte[] b = new byte[bs.length];

            for(int n = 0; n < b.length; ++n) {
                int x = Integer.decode("0X" + bs[n]);
                if (x > 127) {
                    b[n] = (byte)(x - 256);
                } else {
                    b[n] = (byte)x;
                }
            }

            return b;
        }
    }

    public static String byte2hex(byte[] bytes) {
        StringBuffer hs = new StringBuffer("");
        String stmp = "";

        for(int n = 0; n < bytes.length; ++n) {
            stmp = Integer.toHexString(bytes[n] & 255);
            if (stmp.length() == 1) {
                hs.append("0");
            }

            hs.append(stmp);
        }

        return hs.toString().toUpperCase();
    }
}