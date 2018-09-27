/**
 * 
 */
package test;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
public class ClientIPReportor {

    private static String KEY = "react";
    private static String SECRET = "aquickbr-ownf-oxju-mpso-verthelazydo";
    private static String REPORT_URL = "http://www.octopusdio.com/general/ip/report";

    /**
     * @param args
     */
    public static void main(String[] args) {
        ClientIPReportor reportor = new ClientIPReportor();
        Map<String, String> keyValues = new HashMap<String, String>();
        String currentIp = "127.0.0.1";
        try {
            InetAddress iAddress = InetAddress.getLocalHost();
            currentIp = iAddress.getHostAddress();
            System.out.println("Current IP address : " +currentIp);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        keyValues.put("ts", String.valueOf(System.currentTimeMillis()));
        keyValues.put("client_id", "123");
        keyValues.put("client_name", "Golden Key");
        keyValues.put("local_ip", currentIp);
        reportor.report(keyValues);
    }

    public void report(Map<String, String> keyValues) {
        try {
            JSONObject data = new JSONObject();
            for (Iterator<String> it = keyValues.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                String value = keyValues.get(key);
                data.put(key, value);
            }
            String sign = this.generateSign(keyValues, KEY, SECRET);
            System.out.print("sign is:" + sign);
            data.put("key", KEY);
            data.put("sign", sign);
            Header[] headers = new Header[] { new BasicHeader("ORIGIN", "http://www.octopusdio.com"),
                    new BasicHeader("Content-Type", "application/json; charset=utf-8") };
            JSONObject result = WebUtils.postWithJsonInBody(REPORT_URL, data, headers);
            System.out.println("result is:" + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String generateSign(Map<String, String> keyValues, String appKey, String secretKey)
            throws UnsupportedEncodingException {
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
        String secretText = secretKey + "from=" + appKey + paramStr;
        return DigestUtils.sha1Hex(secretText);
    }
}
