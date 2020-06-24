/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
public class SignTest {

    public String generateSign(String jsonStr, String consumerKey, String secret) {
        JSONObject json = JSONObject.fromObject(jsonStr);
        Map<String, String> keyValues = new HashMap<String, String>();
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            if ("key".equals(key) || "sign".equals(key)) {
                continue;
            }
            String value = json.optString(key);
            keyValues.put(key, value);
        }
        return generateSign(keyValues, consumerKey, secret);
    }

    public String generateSign(Map<String, String> keyValues, String consumerKey, String secret) {
        String paramStr = "";
        List<String> sortedKeys = new ArrayList<String>(keyValues.size());
        sortedKeys.addAll(keyValues.keySet());
        Collections.sort(sortedKeys);
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            String value = keyValues.get(key);
            if (null == key || "".equals(key) || null == value || "".equals(value)) {
                continue;
            }
            paramStr += key + "=" + value;
        }
        String secretText = secret + "from=" + consumerKey + paramStr;
        System.out.println("final secret text is:" + secretText);
        return DigestUtils.sha256Hex(secretText);
    }

    /**
     * @param args
     */
    public static final void main(String[] args) {
        String consumerKey = "goldkey";
        String secret = "e36bac5e2ac244f7a6e81b8e43fa92a2";
        String jsonStr = "{\n" + 
                "\"phone\" : \"17764567130\",\n" + 
                "\"number\": \"127\",\n" + 
                "\"avatar\": \"http:www.aaa.com\",\n" + 
                "\"nonce\":1539247432119,\n" + 
                "\"key\":\"goldkey\",\n" + 
                "\"sign\":\"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\"\n" + 
                "}\n" + 
                "";
        try {
            SignTest st = new SignTest();
            String sign = st.generateSign(jsonStr, consumerKey, secret);
            System.out.println("sign is:" + sign);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
