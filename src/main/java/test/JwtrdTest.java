/**
 * 
 */
package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
public class JwtrdTest {

    public static void main(String[] args) {
        String jsonStr = "[\n" + 
                "{\"user_name\":\"毛科栋\",\"idcard_no\":\"3.30227E+17\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"尤翔远\",\"idcard_no\":\"330226199310151595\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"马佳彬\",\"idcard_no\":\"330683199206132019\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"谷秀青\",\"idcard_no\":\"340521199302233319\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"童乐萍\",\"idcard_no\":\"321088199407046123\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"许诺\",\"idcard_no\":\"330106199002040411\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"姚伟锦\",\"idcard_no\":\"440182199410123939\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"严蓉\",\"idcard_no\":\"320911199306056028\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"王慧敏\",\"idcard_no\":\"331081198709191425\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"王统仁\",\"idcard_no\":\"350181198701281950\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"王雪峰\",\"idcard_no\":\"412724198709167419\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"王宜将\",\"idcard_no\":\"370882198905071611\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"张晓梅\",\"idcard_no\":\"130435198704261223\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"孙颢萌\",\"idcard_no\":\"210603199109162049\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"林昱川\",\"idcard_no\":\"350823199102210017\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "{\"user_name\":\"梁春冉\",\"idcard_no\":\"130521199302192773\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"\"},\n" + 
                "\n" + 
                "]";
        JSONArray arr = JSONArray.fromObject(jsonStr);
        JwtrdTest t = new JwtrdTest();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            try {
                t.exportJwtrdImg(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exportJwtrdImg(JSONObject json) throws IOException {
        String id = json.getString("idcard_no");
        String imgFile = json.getString("user_name") + ".png";
        String dir = "jwtrd";
        // get key and sign
        JSONObject input = new JSONObject();
        input.put("id_type", "0");
        input.put("id", id);
        input.put("consumerKey", "hengtejia");
        input.put("secret", "b7ae0ceb39d54d0cb5447fc241bc3f70fc29c4d9e596442c8cbf19ca2d8ff556");
        String signJsonStr = WebUtils.getJsonStrFromPostUrl("http://auth-api.amillionwhy.com/api/generate/sign",
                input.toString());
        JSONObject signJson = JSONObject.fromObject(signJsonStr);
        System.out.println("nonce is:" + signJson.getString("nonce"));
        System.out.println("sign is:" + signJson.getString("sign"));
        // get image
        input = new JSONObject();
        input.put("id_type", "0");
        input.put("id", id);
        input.put("consumerKey", "hengtejia");
        input.put("nonce", signJson.getString("nonce"));
        input.put("sign", signJson.getString("sign"));
        downloadFileWithPostRequest("http://auth-api.xskey.cn/api/jwt/talent/category/cert", input.toString(),
                dir, imgFile);
    }

    /**
     * post请求下载image
     * 
     * @param url
     *            请求地址
     * @param filePath
     *            image文件保存路径
     * @return
     * @throws IOException
     */
    public String downloadFileWithPostRequest(String url, String inputJson, String outDir, String outFile) throws IOException {
        File f = new File(outDir);
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(outDir + "/" + outFile);
        if (!f.exists()) {
            f.createNewFile();
        }
        System.out.println("file path name is:" + f.getAbsolutePath());
        FileOutputStream out = null;
        JSONObject result = new JSONObject();
        int bufferSize = 1024;
        try {
            out = new FileOutputStream(f);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json");
            post.setConfig(createRequestConfig());
            post.setEntity(new StringEntity(inputJson, StandardCharsets.UTF_8));
            try {
                HttpResponse response = client.execute(post);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200 || statusCode == 201) {
                    /* 读返回数据 */
                    InputStream in = response.getEntity().getContent();
                    byte[] buf = new byte[bufferSize];
                    int len = in.read(buf);
                    while (len > 0) {
                        if (len >= bufferSize) {
                            out.write(buf);
                        } else {
                            byte[] buf2 = new byte[len];
                            for (int i = 0; i < len; i++) {
                                buf2[i] = buf[i];
                            }
                            out.write(buf2);
                        }
                        len = in.read(buf);
                    }
                    out.flush();
                } else {
                    String entity = EntityUtils.toString(response.getEntity(), "UTF-8");
                    // LOG.info("status error:"+ statusCode + entity);
                    result.put("msg", entity);
                }
                result.put("statusCode", statusCode);
                return result.toString();
            } catch (ClientProtocolException ex) {
                result.put("msg", ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                result.put("msg", ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            result.put("msg", ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return result.toString();
    }

    private static RequestConfig createRequestConfig() {
        RequestConfig config = RequestConfig.custom().setSocketTimeout(4000).setConnectTimeout(2000)
                .setConnectionRequestTimeout(2000).setStaleConnectionCheckEnabled(true).build();
        return config;
    }

}
