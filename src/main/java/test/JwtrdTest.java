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
        String jsonStr = "[\n"
                + "{\"user_name\":\"焦国泰\",\"idcard_no\":\"422823199508294476\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-8907043468147537070\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"李迎旭\",\"idcard_no\":\"340826199104030850\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-5816720979247804512\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"何俊贤\",\"idcard_no\":\"421127198908143521\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-4198547954896188769\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"吴云\",\"idcard_no\":\"340311198806101928\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"1168549759605883473\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"李伯玮\",\"idcard_no\":\"654001198712111436\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"4654899874209089275\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"郭庆林\",\"idcard_no\":\"410522199104043236\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-7450990206056837601\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"马媛\",\"idcard_no\":\"331021198401191262\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"5743234085635637862\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"魏林盛\",\"idcard_no\":\"342224199108300033\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-4798689438294650961\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"张贵成\",\"idcard_no\":\"340321198801207072\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-7469832146905117099\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"郑露真\",\"idcard_no\":\"330881198909270729\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-8884672740363517423\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"李佶谦\",\"idcard_no\":\"430406199311010048\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-2221323954610420577\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/07/01/\"},\n"
                + "{\"user_name\":\"张鸿飞\",\"idcard_no\":\"330521199107090517\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-455029934950757566\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"蔡子婷\",\"idcard_no\":\"530102199212021122\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-1414106906219362112\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"寿建烽\",\"idcard_no\":\"339005199311071613\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-3118293591488051344\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"李莹\",\"idcard_no\":\"330621198901136384\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-3555022688460782056\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"潘方依\",\"idcard_no\":\"339005199207175121\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"3715653725807462306\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/07/01/\"},\n"
                + "{\"user_name\":\"赵艳超\",\"idcard_no\":\"412827198706177536\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"252694185890693460\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"田朋飞\",\"idcard_no\":\"341221198712051294\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-4270423258506114715\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/29/\"},\n"
                + "{\"user_name\":\"胡常征\",\"idcard_no\":\"34242619860512261X\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-3437999261800903833\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"},\n"
                + "{\"user_name\":\"刘秋荣\",\"idcard_no\":\"350821198807220437\",\"personnel_type\":\"6\",\"personnel_type_name\":\"F类人才\",\"talent_image\":\"-4530518311297070104\",\"file_dir\":\"/home/hengtj/server/file_upload/other/2019/06/28/\"}\n"
                + "]";
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
        String imgFile = json.getString("talent_image");
        String dir = json.getString("file_dir");
        dir = dir.replace("/", "_");
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
