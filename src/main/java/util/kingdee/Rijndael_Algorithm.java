package util.kingdee;

import java.security.InvalidKeyException;

public final class Rijndael_Algorithm {
    static final int[] alog = new int[256];
    static final int[] log = new int[256];
    static final byte[] S = new byte[256];
    static final byte[] Si = new byte[256];
    static final int[] T1 = new int[256];
    static final int[] T2 = new int[256];
    static final int[] T3 = new int[256];
    static final int[] T4 = new int[256];
    static final int[] T5 = new int[256];
    static final int[] T6 = new int[256];
    static final int[] T7 = new int[256];
    static final int[] T8 = new int[256];
    static final int[] U1 = new int[256];
    static final int[] U2 = new int[256];
    static final int[] U3 = new int[256];
    static final int[] U4 = new int[256];
    static final byte[] rcon = new byte[30];
    static final int[][][] shifts = new int[][][]{{{0, 0}, {1, 3}, {2, 2}, {3, 1}}, {{0, 0}, {1, 5}, {2, 4}, {3, 3}}, {{0, 0}, {1, 7}, {3, 5}, {4, 4}}};
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public Rijndael_Algorithm() {
    }


    static final int mul(int a, int b) {
        return a != 0 && b != 0 ? alog[(log[a & 255] + log[b & 255]) % 255] : 0;
    }

    static final int mul4(int a, byte[] b) {
        if (a == 0) {
            return 0;
        } else {
            a = log[a & 255];
            int a0 = b[0] != 0 ? alog[(a + log[b[0] & 255]) % 255] & 255 : 0;
            int a1 = b[1] != 0 ? alog[(a + log[b[1] & 255]) % 255] & 255 : 0;
            int a2 = b[2] != 0 ? alog[(a + log[b[2] & 255]) % 255] & 255 : 0;
            int a3 = b[3] != 0 ? alog[(a + log[b[3] & 255]) % 255] & 255 : 0;
            return a0 << 24 | a1 << 16 | a2 << 8 | a3;
        }
    }


    public static byte[] blockEncrypt(byte[] in, int inOffset, Object sessionKey) {
        int[][] Ke = (int[][])((int[][])((Object[])((Object[])sessionKey))[0]);
        int ROUNDS = Ke.length - 1;
        int[] Ker = Ke[0];
        int t0 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Ker[0];
        int t1 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Ker[1];
        int t2 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Ker[2];
        int t3 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Ker[3];

        for(int r = 1; r < ROUNDS; ++r) {
            Ker = Ke[r];
            int a0 = T1[t0 >>> 24 & 255] ^ T2[t1 >>> 16 & 255] ^ T3[t2 >>> 8 & 255] ^ T4[t3 & 255] ^ Ker[0];
            int a1 = T1[t1 >>> 24 & 255] ^ T2[t2 >>> 16 & 255] ^ T3[t3 >>> 8 & 255] ^ T4[t0 & 255] ^ Ker[1];
            int a2 = T1[t2 >>> 24 & 255] ^ T2[t3 >>> 16 & 255] ^ T3[t0 >>> 8 & 255] ^ T4[t1 & 255] ^ Ker[2];
            int a3 = T1[t3 >>> 24 & 255] ^ T2[t0 >>> 16 & 255] ^ T3[t1 >>> 8 & 255] ^ T4[t2 & 255] ^ Ker[3];
            t0 = a0;
            t1 = a1;
            t2 = a2;
            t3 = a3;
        }

        byte[] result = new byte[16];
        Ker = Ke[ROUNDS];
        int tt = Ker[0];
        result[0] = (byte)(S[t0 >>> 24 & 255] ^ tt >>> 24);
        result[1] = (byte)(S[t1 >>> 16 & 255] ^ tt >>> 16);
        result[2] = (byte)(S[t2 >>> 8 & 255] ^ tt >>> 8);
        result[3] = (byte)(S[t3 & 255] ^ tt);
        tt = Ker[1];
        result[4] = (byte)(S[t1 >>> 24 & 255] ^ tt >>> 24);
        result[5] = (byte)(S[t2 >>> 16 & 255] ^ tt >>> 16);
        result[6] = (byte)(S[t3 >>> 8 & 255] ^ tt >>> 8);
        result[7] = (byte)(S[t0 & 255] ^ tt);
        tt = Ker[2];
        result[8] = (byte)(S[t2 >>> 24 & 255] ^ tt >>> 24);
        result[9] = (byte)(S[t3 >>> 16 & 255] ^ tt >>> 16);
        result[10] = (byte)(S[t0 >>> 8 & 255] ^ tt >>> 8);
        result[11] = (byte)(S[t1 & 255] ^ tt);
        tt = Ker[3];
        result[12] = (byte)(S[t3 >>> 24 & 255] ^ tt >>> 24);
        result[13] = (byte)(S[t0 >>> 16 & 255] ^ tt >>> 16);
        result[14] = (byte)(S[t1 >>> 8 & 255] ^ tt >>> 8);
        result[15] = (byte)(S[t2 & 255] ^ tt);
        return result;
    }

    public static byte[] blockDecrypt(byte[] in, int inOffset, Object sessionKey) {
        int[][] Kd = (int[][])((int[][])((Object[])((Object[])sessionKey))[1]);
        int ROUNDS = Kd.length - 1;
        int[] Kdr = Kd[0];
        int t0 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Kdr[0];
        int t1 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Kdr[1];
        int t2 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Kdr[2];
        int t3 = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Kdr[3];

        for(int r = 1; r < ROUNDS; ++r) {
            Kdr = Kd[r];
            int a0 = T5[t0 >>> 24 & 255] ^ T6[t3 >>> 16 & 255] ^ T7[t2 >>> 8 & 255] ^ T8[t1 & 255] ^ Kdr[0];
            int a1 = T5[t1 >>> 24 & 255] ^ T6[t0 >>> 16 & 255] ^ T7[t3 >>> 8 & 255] ^ T8[t2 & 255] ^ Kdr[1];
            int a2 = T5[t2 >>> 24 & 255] ^ T6[t1 >>> 16 & 255] ^ T7[t0 >>> 8 & 255] ^ T8[t3 & 255] ^ Kdr[2];
            int a3 = T5[t3 >>> 24 & 255] ^ T6[t2 >>> 16 & 255] ^ T7[t1 >>> 8 & 255] ^ T8[t0 & 255] ^ Kdr[3];
            t0 = a0;
            t1 = a1;
            t2 = a2;
            t3 = a3;
        }

        byte[] result = new byte[16];
        Kdr = Kd[ROUNDS];
        int tt = Kdr[0];
        result[0] = (byte)(Si[t0 >>> 24 & 255] ^ tt >>> 24);
        result[1] = (byte)(Si[t3 >>> 16 & 255] ^ tt >>> 16);
        result[2] = (byte)(Si[t2 >>> 8 & 255] ^ tt >>> 8);
        result[3] = (byte)(Si[t1 & 255] ^ tt);
        tt = Kdr[1];
        result[4] = (byte)(Si[t1 >>> 24 & 255] ^ tt >>> 24);
        result[5] = (byte)(Si[t0 >>> 16 & 255] ^ tt >>> 16);
        result[6] = (byte)(Si[t3 >>> 8 & 255] ^ tt >>> 8);
        result[7] = (byte)(Si[t2 & 255] ^ tt);
        tt = Kdr[2];
        result[8] = (byte)(Si[t2 >>> 24 & 255] ^ tt >>> 24);
        result[9] = (byte)(Si[t1 >>> 16 & 255] ^ tt >>> 16);
        result[10] = (byte)(Si[t0 >>> 8 & 255] ^ tt >>> 8);
        result[11] = (byte)(Si[t3 & 255] ^ tt);
        tt = Kdr[3];
        result[12] = (byte)(Si[t3 >>> 24 & 255] ^ tt >>> 24);
        result[13] = (byte)(Si[t2 >>> 16 & 255] ^ tt >>> 16);
        result[14] = (byte)(Si[t1 >>> 8 & 255] ^ tt >>> 8);
        result[15] = (byte)(Si[t0 & 255] ^ tt);
        return result;
    }

    public static synchronized Object makeKey(byte[] k, int blockSize) throws InvalidKeyException {
        if (k == null) {
            throw new InvalidKeyException("Empty key");
        } else if (k.length != 16 && k.length != 24 && k.length != 32) {
            throw new InvalidKeyException("Incorrect key length");
        } else {
            int ROUNDS = getRounds(k.length, blockSize);
            int BC = blockSize / 4;
            int[][] Ke = new int[ROUNDS + 1][BC];
            int[][] Kd = new int[ROUNDS + 1][BC];
            int ROUND_KEY_COUNT = (ROUNDS + 1) * BC;
            int KC = k.length / 4;
            int[] tk = new int[KC];
            int i = 0;

            int j;
            for(j = 0; i < KC; tk[i++] = (k[j++] & 255) << 24 | (k[j++] & 255) << 16 | (k[j++] & 255) << 8 | k[j++] & 255) {
            }

            int t = 0;

            for(j = 0; j < KC && t < ROUND_KEY_COUNT; ++t) {
                Ke[t / BC][t % BC] = tk[j];
                Kd[ROUNDS - t / BC][t % BC] = tk[j];
                ++j;
            }

            int rconpointer = 0;

            int tt;
            while(t < ROUND_KEY_COUNT) {
                tt = tk[KC - 1];
                tk[0] ^= (S[tt >>> 16 & 255] & 255) << 24 ^ (S[tt >>> 8 & 255] & 255) << 16 ^ (S[tt & 255] & 255) << 8 ^ S[tt >>> 24 & 255] & 255 ^ (rcon[rconpointer++] & 255) << 24;
                int var10001;
                if (KC != 8) {
                    i = 1;

                    for(j = 0; i < KC; tk[var10001] ^= tk[j++]) {
                        var10001 = i++;
                    }
                } else {
                    i = 1;

                    for(j = 0; i < KC / 2; tk[var10001] ^= tk[j++]) {
                        var10001 = i++;
                    }

                    tt = tk[KC / 2 - 1];
                    tk[KC / 2] ^= S[tt & 255] & 255 ^ (S[tt >>> 8 & 255] & 255) << 8 ^ (S[tt >>> 16 & 255] & 255) << 16 ^ (S[tt >>> 24 & 255] & 255) << 24;
                    j = KC / 2;

                    for(i = j + 1; i < KC; tk[var10001] ^= tk[j++]) {
                        var10001 = i++;
                    }
                }

                for(j = 0; j < KC && t < ROUND_KEY_COUNT; ++t) {
                    Ke[t / BC][t % BC] = tk[j];
                    Kd[ROUNDS - t / BC][t % BC] = tk[j];
                    ++j;
                }
            }

            for(int r = 1; r < ROUNDS; ++r) {
                for(j = 0; j < BC; ++j) {
                    tt = Kd[r][j];
                    Kd[r][j] = U1[tt >>> 24 & 255] ^ U2[tt >>> 16 & 255] ^ U3[tt >>> 8 & 255] ^ U4[tt & 255];
                }
            }

            Object[] sessionKey = new Object[]{Ke, Kd};
            return sessionKey;
        }
    }

    public static byte[] blockEncrypt(byte[] in, int inOffset, Object sessionKey, int blockSize) {
        if (blockSize == 16) {
            return blockEncrypt(in, inOffset, sessionKey);
        } else {
            Object[] sKey = (Object[])((Object[])sessionKey);
            int[][] Ke = (int[][])((int[][])sKey[0]);
            int BC = blockSize / 4;
            int ROUNDS = Ke.length - 1;
            int SC = BC == 4 ? 0 : (BC == 6 ? 1 : 2);
            int s1 = shifts[SC][1][0];
            int s2 = shifts[SC][2][0];
            int s3 = shifts[SC][3][0];
            int[] a = new int[BC];
            int[] t = new int[BC];
            byte[] result = new byte[blockSize];
            int j = 0;

            int i;
            for(i = 0; i < BC; ++i) {
                t[i] = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Ke[0][i];
            }

            for(int r = 1; r < ROUNDS; ++r) {
                for(i = 0; i < BC; ++i) {
                    a[i] = T1[t[i] >>> 24 & 255] ^ T2[t[(i + s1) % BC] >>> 16 & 255] ^ T3[t[(i + s2) % BC] >>> 8 & 255] ^ T4[t[(i + s3) % BC] & 255] ^ Ke[r][i];
                }

                System.arraycopy(a, 0, t, 0, BC);
            }

            for(i = 0; i < BC; ++i) {
                int tt = Ke[ROUNDS][i];
                result[j++] = (byte)(S[t[i] >>> 24 & 255] ^ tt >>> 24);
                result[j++] = (byte)(S[t[(i + s1) % BC] >>> 16 & 255] ^ tt >>> 16);
                result[j++] = (byte)(S[t[(i + s2) % BC] >>> 8 & 255] ^ tt >>> 8);
                result[j++] = (byte)(S[t[(i + s3) % BC] & 255] ^ tt);
            }

            return result;
        }
    }

    public static byte[] blockDecrypt(byte[] in, int inOffset, Object sessionKey, int blockSize) {
        if (blockSize == 16) {
            return blockDecrypt(in, inOffset, sessionKey);
        } else {
            Object[] sKey = (Object[])((Object[])sessionKey);
            int[][] Kd = (int[][])((int[][])sKey[1]);
            int BC = blockSize / 4;
            int ROUNDS = Kd.length - 1;
            int SC = BC == 4 ? 0 : (BC == 6 ? 1 : 2);
            int s1 = shifts[SC][1][1];
            int s2 = shifts[SC][2][1];
            int s3 = shifts[SC][3][1];
            int[] a = new int[BC];
            int[] t = new int[BC];
            byte[] result = new byte[blockSize];
            int j = 0;

            int i;
            for(i = 0; i < BC; ++i) {
                t[i] = ((in[inOffset++] & 255) << 24 | (in[inOffset++] & 255) << 16 | (in[inOffset++] & 255) << 8 | in[inOffset++] & 255) ^ Kd[0][i];
            }

            for(int r = 1; r < ROUNDS; ++r) {
                for(i = 0; i < BC; ++i) {
                    a[i] = T5[t[i] >>> 24 & 255] ^ T6[t[(i + s1) % BC] >>> 16 & 255] ^ T7[t[(i + s2) % BC] >>> 8 & 255] ^ T8[t[(i + s3) % BC] & 255] ^ Kd[r][i];
                }

                System.arraycopy(a, 0, t, 0, BC);
            }

            for(i = 0; i < BC; ++i) {
                int tt = Kd[ROUNDS][i];
                result[j++] = (byte)(Si[t[i] >>> 24 & 255] ^ tt >>> 24);
                result[j++] = (byte)(Si[t[(i + s1) % BC] >>> 16 & 255] ^ tt >>> 16);
                result[j++] = (byte)(Si[t[(i + s2) % BC] >>> 8 & 255] ^ tt >>> 8);
                result[j++] = (byte)(Si[t[(i + s3) % BC] & 255] ^ tt);
            }

            return result;
        }
    }

    private static boolean self_test(int keysize) {
        boolean ok = false;

        try {
            byte[] kb = new byte[keysize];
            byte[] pt = new byte[16];

            int i;
            for(i = 0; i < keysize; ++i) {
                kb[i] = (byte)i;
            }

            for(i = 0; i < 16; ++i) {
                pt[i] = (byte)i;
            }

            Object key = makeKey(kb, 16);
            byte[] ct = blockEncrypt(pt, 0, key, 16);
            byte[] cpt = blockDecrypt(ct, 0, key, 16);
            ok = areEqual(pt, cpt);
            if (!ok) {
                throw new RuntimeException("Symmetric operation failed");
            }
        } catch (Exception var8) {
        }

        return ok;
    }

    public static int getRounds(int keySize, int blockSize) {
        switch (keySize) {
            case 16:
                return blockSize == 16 ? 10 : (blockSize == 24 ? 12 : 14);
            case 24:
                return blockSize != 32 ? 12 : 14;
            default:
                return 14;
        }
    }

    public static boolean areEqual(byte[] a, byte[] b) {
        int aLength = a.length;
        if (aLength != b.length) {
            return false;
        } else {
            for(int i = 0; i < aLength; ++i) {
                if (a[i] != b[i]) {
                    return false;
                }
            }

            return true;
        }
    }

    public static String toString(byte[] ba) {
        int length = ba.length;
        char[] buf = new char[length * 2];
        int i = 0;

        byte k;
        for(int j = 0; i < length; buf[j++] = HEX_DIGITS[k & 15]) {
            k = ba[i++];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
        }

        return new String(buf);
    }

    static {
        long time = System.currentTimeMillis();
        int ROOT = 283;
        int j = 0;
        alog[0] = 1;

        int i;
        for(i = 1; i < 256; ++i) {
            j = alog[i - 1] << 1 ^ alog[i - 1];
            if ((j & 256) != 0) {
                j ^= ROOT;
            }

            alog[i] = j;
        }

        for(i = 1; i < 255; log[alog[i]] = i++) {
        }

        byte[][] A = new byte[][]{{1, 1, 1, 1, 1, 0, 0, 0}, {0, 1, 1, 1, 1, 1, 0, 0}, {0, 0, 1, 1, 1, 1, 1, 0}, {0, 0, 0, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 0, 1, 1, 1}, {1, 1, 1, 0, 0, 0, 1, 1}, {1, 1, 1, 1, 0, 0, 0, 1}};
        byte[] B = new byte[]{0, 1, 1, 0, 0, 0, 1, 1};
        byte[][] box = new byte[256][8];
        box[1][7] = 1;

        int t;
        for(i = 2; i < 256; ++i) {
            j = alog[255 - log[i]];

            for(t = 0; t < 8; ++t) {
                box[i][t] = (byte)(j >>> 7 - t & 1);
            }
        }

        byte[][] cox = new byte[256][8];

        for(i = 0; i < 256; ++i) {
            for(t = 0; t < 8; ++t) {
                cox[i][t] = B[t];

                for(j = 0; j < 8; ++j) {
                    cox[i][t] = (byte)(cox[i][t] ^ A[t][j] * box[i][j]);
                }
            }
        }

        for(i = 0; i < 256; ++i) {
            S[i] = (byte)(cox[i][0] << 7);

            for(t = 1; t < 8; ++t) {
                byte[] var10000 = S;
                var10000[i] = (byte)(var10000[i] ^ cox[i][t] << 7 - t);
            }

            Si[S[i] & 255] = (byte)i;
        }

        byte[][] G = new byte[][]{{2, 1, 1, 3}, {3, 2, 1, 1}, {1, 3, 2, 1}, {1, 1, 3, 2}};
        byte[][] AA = new byte[4][8];

        for(i = 0; i < 4; ++i) {
            for(j = 0; j < 4; ++j) {
                AA[i][j] = G[i][j];
            }

            AA[i][i + 4] = 1;
        }

        byte[][] iG = new byte[4][4];

        for(i = 0; i < 4; ++i) {
            byte pivot = AA[i][i];
            if (pivot == 0) {
                for(t = i + 1; AA[t][i] == 0 && t < 4; ++t) {
                }

                if (t == 4) {
                    throw new RuntimeException("G matrix is not invertible");
                }

                for(j = 0; j < 8; ++j) {
                    byte tmp = AA[i][j];
                    AA[i][j] = AA[t][j];
                    AA[t][j] = tmp;
                }

                pivot = AA[i][i];
            }

            for(j = 0; j < 8; ++j) {
                if (AA[i][j] != 0) {
                    AA[i][j] = (byte)alog[(255 + log[AA[i][j] & 255] - log[pivot & 255]) % 255];
                }
            }

            for(t = 0; t < 4; ++t) {
                if (i != t) {
                    for(j = i + 1; j < 8; ++j) {
                        AA[t][j] = (byte)(AA[t][j] ^ mul(AA[i][j], AA[t][i]));
                    }

                    AA[t][i] = 0;
                }
            }
        }

        for(i = 0; i < 4; ++i) {
            for(j = 0; j < 4; ++j) {
                iG[i][j] = AA[i][j + 4];
            }
        }

        for(t = 0; t < 256; ++t) {
            int s = S[t];
            T1[t] = mul4(s, G[0]);
            T2[t] = mul4(s, G[1]);
            T3[t] = mul4(s, G[2]);
            T4[t] = mul4(s, G[3]);
            s = Si[t];
            T5[t] = mul4(s, iG[0]);
            T6[t] = mul4(s, iG[1]);
            T7[t] = mul4(s, iG[2]);
            T8[t] = mul4(s, iG[3]);
            U1[t] = mul4(t, iG[0]);
            U2[t] = mul4(t, iG[1]);
            U3[t] = mul4(t, iG[2]);
            U4[t] = mul4(t, iG[3]);
        }

        rcon[0] = 1;
        int r = 1;

        for(t = 1; t < 30; rcon[t++] = (byte)(r = mul(2, r))) {
        }

        time = System.currentTimeMillis() - time;
    }
}
