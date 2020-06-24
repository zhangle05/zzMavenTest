package test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static final String ENCODING = "utf-8";

    private static String key = "81eb6d792cd7db84";

    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, ENCODING);
                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String Decrypt(byte[] encrypted, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original, ENCODING);
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 判断Key是否正确
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes(ENCODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(ENCODING));
        return byte2hex(encrypted).toLowerCase();
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    
    /**
     * 密钥、初始向量都是：1234567890123456
原文(ASCII)：0123456789012345
密文(16进制)：9D 2E 5A B8 F8 E7 7E B2 4B 49 5D D4 C7 AC A7 62

原文(ASCII)：
{
    "id":1234567,
    "longitude":120.12345678,
    "latitude":35.87654321,
    "altitude":235.43,
    "temperature":3.5,
    "battery":0.85,
    "life":1250
}
密文(16进制)：39 65 3E 6B 21 FC AD A5 45 83 AF 1D 9A D0 29 D6 DB 11 5A EB 78 73 41 41 DA 8A D7 9D 66 3A 1D 11 04 26 9C 45 5D 1A 39 68 D7 03 BC 0C 5A 1E 14 C3 52 3C E4 50 3E AB FD 0A CE AC 44 B2 B5 A1 25 92 2E C2 86 D4 16 8F CB 5E 61 28 B6 A6 AD 13 C8 43 9A E3 22 B8 32 1C 85 A5 FA 65 C9 71 1C A1 D0 CC F5 94 ED 02 7F B0 4A AC 44 09 F8 93 31 CD B3 1C 12 7B 27 45 CA A7 FD DF E6 0C 1D C3 89 5F B0 A1 03 ED 5B 7E EA CF 00 28 D8 A0 22 38 17 44 9D 4F AC 9F BD 61 CE 8A C8 5B 92 E2 A0 D4 38 AC 32 43 61 97 49 48 A2 FF 94 D5 9E B7 D8 16 42 F7 6F E9
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(Encrypt("测试", key));
        System.out.println(Decrypt("5aa08246bd8f0d0408aed3acc8649306", key));
        System.out.println(Decrypt("9D2E5AB8F8E77EB24B495DD4C7ACA762", key));

//        String encode = Encrypt("", key);
//        System.out.println("encode:" + encode + ".");
//        String decode = Decrypt(encode, key);
//        System.out.println("decode:" + decode + ".");
//        encode = Encrypt("user=21&code=aaaa-bbb-cccc-ddd", key);
//        System.out.println("encode:" + encode + ".");
//        decode = Decrypt(encode, key);
//        System.out.println("decode:" + decode + ".");
//        System.out.println("-------------------------------------------");
//        byte bytes[] = hex2byte("9D2E5AB8F8E77EB24B495DD4C7ACA762");
        
        byte bytes[] = hex2byte("9D 2E 5A B8 F8 E7 7E B2 4B 49 5D D4 C7 AC A7 62");
        System.out.println(Decrypt("9D2E5AB8F8E77EB24B495DD4C7ACA762", "1234567890123456"));
        String tmp = "39 65 3E 6B 21 FC AD A5 45 83 AF 1D 9A D0 29 D6 DB 11 5A EB 78 73 41 41 DA 8A D7 9D 66 3A 1D 11 04 26 9C 45 5D 1A 39 68 D7 03 BC 0C 5A 1E 14 C3 52 3C E4 50 3E AB FD 0A CE AC 44 B2 B5 A1 25 92 2E C2 86 D4 16 8F CB 5E 61 28 B6 A6 AD 13 C8 43 9A E3 22 B8 32 1C 85 A5 FA 65 C9 71 1C A1 D0 CC F5 94 ED 02 7F B0 4A AC 44 09 F8 93 31 CD B3 1C 12 7B 27 45 CA A7 FD DF E6 0C 1D C3 89 5F B0 A1 03 ED 5B 7E EA CF 00 28 D8 A0 22 38 17 44 9D 4F AC 9F BD 61 CE 8A C8 5B 92 E2 A0 D4 38 AC 32 43 61 97 49 48 A2 FF 94 D5 9E B7 D8 16 42 F7 6F E9";
        System.out.println(tmp.replace(" ", ""));

    }
}
