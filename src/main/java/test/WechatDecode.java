/**
 * 
 */
package test;

import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

/**
 * @author zhangle
 *
 */
public class WechatDecode {

    public String decrypt(String sessionKey, String iv, String encryptedData) {
        try {
            byte[] dataBytes = Base64.decode(encryptedData);
            byte[] keyBytes = Base64.decode(sessionKey);
            byte[] ivBytes = Base64.decode(iv);
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivBytes));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataBytes);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        WechatDecode wd = new WechatDecode();
        String result = wd.decrypt("", "OJ3EMcNeQws/RtLjrptFaw==",
                "Y5ArBUhy0bPaoeG0cJvcdLJWRgcCXNMnqc85FRBogyrQRZMo+5jqpxnrhPmREKrcN5GWFRCdNwsfM9u67K12Rlam615aBJ0T6w/hGSUtuZ0Pd5AV8J8zxRig8Q1jQPqMCAWzmY+ooz/XhB9T3w9AuFWZOMoZAaJBwdDepAR1a8ja3kbN6D02cWzKKHV+jJmFlnVeA1HG31gseGjN4OxcoQ==");
        System.out.println(result);
    }

}
