/**
 * 
 */
package zz.maven.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import test.WebUtils;

/**
 * @author zhangle
 *
 */
public class JwtAdapter {

    public static final String JWT_APPLICATIONS_URI = "/api/jwt/applications";
    private static final String CONSUMER_KEY = "gold-key";
    private static final String SECRET_KEY = "ExfHIovYhrlT6GdvhgtBr5JC8WWTWs41w7iBe1X4Y92mUlv5rZ0HxZ5nUvAGvijP";
    private static String JWT_SERVER_HOST = System.getenv("JWT_SERVER_HOST");
    private static String JWT_SERVER_PORT = System.getenv("JWT_SERVER_PORT");
    // 金梧桐申请查询地址
    // 人才认定:
    private static final String TALENT_IDENTIFY_URI = "/index.php?/api/person/index";
    // 购房补贴:
    private static final String HOUSE_SUBSIDY_URI = "/index.php?/api/housesell/index";
    // 租房补贴:
    private static final String RENT_SUBSIDY_URI = "/index.php?/api/houserent/index";
    // 生活津贴:
    private static final String LIVING_ALLOWANCE_URI = "/index.php?/api/bounty/index";
    // 应届生生活津贴:
    private static final String STUDENT_ALLOWANCE_URI = "/index.php?/api/grad/index";
    // 实习生津贴:
    private static final String INTERN_ALLOWANCE_URI = "/index.php?/api/internship/index";
    // 贷款贴息:
    private static final String LOAN_ELIGIBLE_URI = "/index.php?/api/loan/index";

    // 申请历史操作查询地址(uri:uri)
    private final Hashtable<String, String> jwtHistoryTable = new Hashtable<String, String>();
    // 申请历史操作缓存(uri-id:jsonData)
    private final Hashtable<String, String> jwtHistoryDataTable = new Hashtable<String, String>();
    private static final long TWO_YEAR_MILS = 3600L * 1000 * 24 * 365 * 2;

    /**
     * Constructor
     */
    public JwtAdapter() {
        // 人才认定审核历史:
        jwtHistoryTable.put(TALENT_IDENTIFY_URI, "/index.php?/api/person/log_action_details");
        // 购房补贴审核历史:
        jwtHistoryTable.put(HOUSE_SUBSIDY_URI, "/index.php?/api/housesell/log_action_details");
        // 租房补贴审核历史:
        jwtHistoryTable.put(RENT_SUBSIDY_URI, "/index.php?/api/houserent/log_action_details");
        // 生活津贴审核历史:
        jwtHistoryTable.put(LIVING_ALLOWANCE_URI, "/index.php?/api/bounty/log_action_details");
        // 应届生生活津贴审核历史:
        jwtHistoryTable.put(STUDENT_ALLOWANCE_URI, "/index.php?/api/grad/log_action_details");
        // 实习生津贴审核历史:
        jwtHistoryTable.put(INTERN_ALLOWANCE_URI, "/index.php?/api/internship/log_action_details");
        // 贷款贴息审核历史:
        jwtHistoryTable.put(LOAN_ELIGIBLE_URI, "/index.php?/api/loan/log_action_details");
    }

    private JSONObject getLatestData(String uri, JSONArray dataArr) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < dataArr.size(); i++) {
            list.add(dataArr.getJSONObject(i));
        }
        list.sort(new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                if (o1.containsKey("created_time")) {
                    String t1 = o1.getString("created_time");
                    String t2 = o2.getString("created_time");
                    return t1.compareTo(t2);
                } else if (o1.containsKey("id")) {
                    String id1 = o1.getString("id");
                    String id2 = o2.getString("id");
                    return id1.compareTo(id2);
                }
                return 0;
            }
        });
        if (TALENT_IDENTIFY_URI.equals(uri)) {
            // special handle for talent-identify
            JSONObject data = getLatestTalentIdentifyData(list);
            if (data != null) {
                return data;
            }
        }
        return list.get(list.size() - 1);
    }

    private JSONObject getLatestTalentIdentifyData(List<JSONObject> sortedDataList) {
        try {
            JSONObject latest = sortedDataList.get(sortedDataList.size() - 1);
            if ("批准通过".equals(latest.getString("t_status"))) {
                // 最新提交的申请也是已经批准通过的申请，不用检查history
                return null;
            }
            if (!"新申请".equals(latest.getString("apply_type"))) {
                // 最新提交的申请不是新申请，是重新备案申请，则不再考虑之前已经批准通过的申请，不用检查history
                return null;
            }
            for (int i = 0; i < sortedDataList.size(); i++) {
                JSONObject data = sortedDataList.get(i);
                System.out.println(data.getString("t_status"));
                if ("批准通过".equals(data.getString("t_status"))) {
                    String id = data.getString("id");
                    if (id.equals(latest.getString("id"))) {
                        // 批准通过申请即为最新提交的申请，不用检查history
                        return null;
                    }
                    String history = syncGetApplicationHistory(id, TALENT_IDENTIFY_URI);
                    JSONObject historyJson = JSONObject.fromObject(history);
                    jwtHistoryDataTable.put(TALENT_IDENTIFY_URI + "-" + id, history);
                    JSONArray logs = historyJson.getJSONObject("data").getJSONArray("list");
                    System.out.println(logs.size());
                    if (logs.size() > 0) {
                        JSONObject latestLog = logs.getJSONObject(logs.size() - 1);
                        String createdTimeStr = latestLog.getString("created_time");
                        long createdTime = Long.parseLong(createdTimeStr) * 1000;
                        System.out.println(System.currentTimeMillis());
                        System.out.println(createdTime);
                        System.out.println(System.currentTimeMillis() - createdTime);
                        if (System.currentTimeMillis() - createdTime < TWO_YEAR_MILS) {
                            return data;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String generateSign(JSONObject json, String consumerKey, String secret) {
        Map<String, String> keyValues = new HashMap<String, String>();
        for (Iterator<String> it = json.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            Object obj = json.get(key);
            String value = obj == null ? "" : obj.toString();
            keyValues.put(key, value);
        }
        return generateSignByMap(keyValues, consumerKey, secret);
    }

    private String generateSignByMap(Map<String, String> keyValues, String consumerKey, String secret) {
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
        return DigestUtils.sha256Hex(secretText);
    }

    private String syncGetApplication(int idType, String id, String uri) {
        JSONObject jwtInput = new JSONObject();
        jwtInput.put("card_id", id);
        jwtInput.put("card_id_type", idType);
        jwtInput.put("nonce", System.currentTimeMillis());
        jwtInput.put("key", CONSUMER_KEY);
        String sign = this.generateSign(jwtInput, CONSUMER_KEY, SECRET_KEY);
        jwtInput.put("sign", sign);

        String url = JWT_SERVER_HOST + ":" + JWT_SERVER_PORT + uri;
        // String url = "http://xsrc.xs.zj.cn" + LIVING_ALLOWANCE_URI;
        return WebUtils.getJsonStrFromPostUrl(url, jwtInput.toString());
    }

    private String syncGetApplicationHistory(String applicationId, String applicationUri) {
        String id = applicationId;
        JSONObject historyInput = new JSONObject();
        historyInput.put("id", id);
        historyInput.put("nonce", System.currentTimeMillis());
        historyInput.put("key", CONSUMER_KEY);
        String sign = this.generateSign(historyInput, CONSUMER_KEY, SECRET_KEY);
        historyInput.put("sign", sign);
        String historyUri = jwtHistoryTable.get(applicationUri);
        String url = JWT_SERVER_HOST + ":" + JWT_SERVER_PORT + historyUri;
        return WebUtils.getJsonStrFromPostUrl(url, historyInput.toString());
    }

    public static void main(String[] args) {
        JWT_SERVER_HOST = "http://xsrc.xs.zj.cn";
        JWT_SERVER_PORT = "80";
        JwtAdapter ja = new JwtAdapter();
        String result = ja.syncGetApplication(0, "522501198301120414", TALENT_IDENTIFY_URI);
        JSONObject json = JSONObject.fromObject(result);
        JSONObject dataJson = json.getJSONObject("data");
        JSONArray arr = dataJson.getJSONArray("list");
        JSONObject item = ja.getLatestData(TALENT_IDENTIFY_URI, arr);
        String history = ja.syncGetApplicationHistory(item.getString("id"), TALENT_IDENTIFY_URI);
        System.out.println(result);
        System.out.println(item.toString());
        System.out.println(history);

    }
}
