/**
 * 
 */
package zz.maven.test;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author zhangle
 *
 */
public class MD5Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        String data = "{\"cno\":\"p020200507\",\"tn\":\"970070f4ef0b469a9ead9716f43635c2\",\"p\":{\"h5Source\": \"p020200507\",\"phone\": \"18301120860\",\"name\": \"张乐\"},\"seqId\":\"134567\"}p020200507";
        String data = "{\"cno\":\"p020200507\",\"tn\":\"970070f4ef0b469a9ead9716f43635c2\",\"p\":{\"h5Source\": \"p020200507\",\"phone\": \"18301120860\",\"name\": \"张乐\"},\"seqId\":\"134567\"}sign";
        String md5 = DigestUtils.md5Hex(data);
        System.out.println("md5:" + md5);
    }

}
