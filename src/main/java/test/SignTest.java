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
        System.out.println("sign of '123' is:" + DigestUtils.sha256Hex("123"));
        return DigestUtils.sha256Hex(secretText);
    }

    /**
     * @param args
     */
    public static final void main(String[] args) {
        String consumerKey = "gold-key";
        String secret = "ExfHIovYhrlT6GdvhgtBr5JC8WWTWs41w7iBe1X4Y92mUlv5rZ0HxZ5nUvAGvijP";
        String jsonStr = "{\n" + 
                "    \"card_id\":\"20181108\",\n" + 
                "    \"card_id_type\":\"3\",\n" + 
                "    \"nonce\":\"1541656524590\"\n" + 
                "}";
        try {
            SignTest st = new SignTest();
            String sign = st.generateSign(jsonStr, consumerKey, secret);
            System.out.println("sign is:" + sign);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
