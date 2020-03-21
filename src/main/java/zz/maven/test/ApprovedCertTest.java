/**
 * 
 */
package zz.maven.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import test.WebUtils;

/**
 * @author zhangle
 *
 */
public class ApprovedCertTest {

    public static void main(String[] args) {
        String excel = "/Users/zhangle/Documents/work/aiit/人才房/准租证及公示模板/需要重新生成准租证的人员名单.xlsx";
        String outDir = "/Users/zhangle/Downloads/准租证";
        ApprovedCertTest t = new ApprovedCertTest();
        try {
            JSONArray idArr = t.getIdJson(excel);
            System.out.println("-------------------");
            JSONArray arr = t.getUserDateJson(excel, idArr);
//            for (int i = 0; i < arr.size(); i++) {
//                JSONObject json = arr.getJSONObject(i);
//                String outFile = json.getString("sn") + ".png";
//                t.exportSingleApprovedCertImg(json, outDir, outFile);
//            }
            String sql = "";
            for (int i = 0; i < arr.size(); i++) {
                JSONObject json = arr.getJSONObject(i);
                String sn = json.getString("sn");
                String imgFile = outDir + "/" + sn + ".png";
                String uploadUrl = "https://htj5.amillionwhy.com/talent-room-file/platform/platformFile/upload";
                JSONObject uploadJson = t.uploadCert(imgFile, uploadUrl);
                String fileId = uploadJson.getString("fileId");
                sql = sql + "update house_apply_permit set permit_image_id=" + fileId + " where permit_sn=\"" + sn
                        + "\";\r\n";
            }
            System.out.println(sql);
        } catch (EncryptedDocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JSONArray getIdJson(String excelFileName) throws EncryptedDocumentException, IOException {
        FileInputStream inputStream = new FileInputStream(new File(excelFileName));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheet("bbb");
        int rowCount = sheet.getLastRowNum();
        JSONArray result = new JSONArray();
        for (int i = 0; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell c = row.getCell(0);
            String applyId = c.getStringCellValue();
            c = row.getCell(1);
            String name = c.getStringCellValue();
            c = row.getCell(2);
            String id = c.getStringCellValue();
            c = row.getCell(3);
            String area = c.getStringCellValue();
            System.out.println(applyId + "-" + name + "-" + id + "-" + area);
            JSONObject json = new JSONObject();
            json.put("applyId", applyId);
            json.put("name", name);
            json.put("id", id);
            json.put("area", area);
            result.add(json);
        }
        System.out.println("row count:" + rowCount);
        return result;
    }

    private JSONArray getUserDateJson(String excelFileName, JSONArray idArr)
            throws EncryptedDocumentException, IOException {
        FileInputStream inputStream = new FileInputStream(new File(excelFileName));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheet("aaa");
        int rowCount = sheet.getLastRowNum();
        JSONArray result = new JSONArray();
        String now = String.valueOf(System.currentTimeMillis());
        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell c = row.getCell(17);
            String type = c.getStringCellValue();
            c = row.getCell(2);
            String sn = c.getStringCellValue();
            c = row.getCell(4);
            String name = c.getStringCellValue();
            c = row.getCell(3);
            String applyId = c.getStringCellValue();
            JSONObject idJson = this.getIdByApplyIdAndName(applyId, name, idArr);
            String id = idJson.getString("id");
            String area = idJson.getString("area").replace("平方米", "");
            c = row.getCell(5);
            String talentName = c.getStringCellValue();
            if ("中高级人才".equals(talentName)) {
                c = row.getCell(6);
                talentName = c.getStringCellValue();
            }
            talentName = talentName.replace("人才", "");
            c = row.getCell(7);
            if (c != null) {
                String isIdentify = c.getStringCellValue();
                if ("yes".equals(isIdentify) && talentName.contains("类")) {
                    talentName = talentName + "[认定中]";
                }
            }
            c = row.getCell(12);
            Date submitDateTime = getDateCellValueByCell(c);
            String submitDateStr = String.valueOf(submitDateTime.getTime());
            c = row.getCell(8);
            String partnerId = "";
            String partnerName = "";
            if (c != null) {
                String isJoint = c.getStringCellValue();
                if ("yes".equals(isJoint)) {
                    c = row.getCell(18);
                    String jointApplyId = c.getStringCellValue();
                    c = row.getCell(19);
                    partnerName = c.getStringCellValue();
                    JSONObject jointJson = this.getIdByApplyIdAndName(jointApplyId, partnerName, idArr);
                    partnerId = jointJson.getString("id");
                }
            }
            System.out.println(type + "-" + sn + "-" + name + "-" + id + "-" + area + "-" + talentName + "-"
                    + submitDateTime + "-" + partnerId + "-" + partnerName);
            JSONObject json = new JSONObject();
            json.put("type", type);
            json.put("sn", sn);
            json.put("name", name);
            json.put("id", id);
            json.put("area", area);
            json.put("talent_name", talentName);
            json.put("time", now);
            json.put("corp_submit_time", submitDateStr);
            if (!StringUtils.isEmpty(partnerId)) {
                json.put("partner_id", partnerId);
                json.put("partner_name", partnerName);
            }
            result.add(json);
        }
        System.out.println("row count:" + rowCount);
        return result;
    }

    private JSONObject getIdByApplyIdAndName(String applyId, String name, JSONArray idArr) {
        for (int i = 0; i < idArr.size(); i++) {
            JSONObject idJson = idArr.getJSONObject(i);
            String applyId2 = idJson.getString("applyId");
            String name2 = idJson.getString("name");
            if (applyId.equals(applyId2) && name.equals(name2)) {
                return idJson;
            }
        }
        return null;
    }

    private void exportSingleApprovedCertImg(JSONObject json, String outDir, String outFile) throws IOException {
        // get key and sign
        json.put("consumerKey", "hengtejia");
        json.put("secret", "b7ae0ceb39d54d0cb5447fc241bc3f70fc29c4d9e596442c8cbf19ca2d8ff556");
        String signJsonStr = WebUtils.getJsonStrFromPostUrl("http://auth-api.xskey.com.cn/api/generate/sign",
                json.toString());
        JSONObject signJson = JSONObject.fromObject(signJsonStr);
        System.out.println("nonce is:" + signJson.getString("nonce"));
        System.out.println("sign is:" + signJson.getString("sign"));
        // get image
        JSONObject input = new JSONObject();
        input.put("type", json.getString("type"));
        input.put("sn", json.getString("sn"));
        input.put("name", json.getString("name"));
        input.put("id", json.getString("id"));
        input.put("area", json.getString("area"));
        input.put("talent_name", json.getString("talent_name"));
        input.put("time", json.getString("time"));
        input.put("corp_submit_time", json.getString("corp_submit_time"));
        if (json.containsKey("partner_id")) {
            input.put("partner_id", json.getString("partner_id"));
            input.put("partner_name", json.getString("partner_name"));
        }
        input.put("consumerKey", "hengtejia");
        input.put("nonce", signJson.getString("nonce"));
        input.put("sign", signJson.getString("sign"));
        downloadFileWithPostRequest("http://auth-api.xskey.cn/api/housing/approved/cert", input.toString(), outDir,
                outFile);
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
    public String downloadFileWithPostRequest(String url, String inputJson, String outDir, String outFile)
            throws IOException {
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

    public static Date getDateCellValueByCell(Cell cell) {
        // 判断是否为null或空串
        if (cell == null || cell.toString().trim().equals("")) {
            return null;
        }
        Date cellValue = null;
        if (DateUtil.isCellDateFormatted(cell)) {
            short format = cell.getCellStyle().getDataFormat();
            SimpleDateFormat sdf = null;
            // System.out.println("cell.getCellStyle().getDataFormat()="+cell.getCellStyle().getDataFormat());
            if (format == 20 || format == 32) {
                sdf = new SimpleDateFormat("HH:mm");
            } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                cellValue = date;
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            try {
                cellValue = cell.getDateCellValue();// 日期
            } catch (Exception e) {
                try {
                    throw new Exception("exception on get date data !".concat(e.toString()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } finally {
                sdf = null;
            }
        } else {
            BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
            cellValue = null;
        }
        return cellValue;
    }

    private JSONObject uploadCert(String filePath, String url) throws ClientProtocolException, IOException {
        String result = WebUtils.uploadFile(filePath, url);
        System.out.println(result);
        JSONObject json = JSONObject.fromObject(result);
        int code = json.getInt("code");
        if (code != 0) {
            throw new IllegalStateException("upload file failed:" + result);
        }
        JSONArray bodyArr = json.getJSONArray("body");
        return bodyArr.getJSONObject(0);
    }
}
