/**
 * 
 */
package test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AftAifinFireeyeOcrImageQueryRequest;
import com.alipay.api.response.AftAifinFireeyeOcrImageQueryResponse;

/**
 * @author zhangle
 *
 */
public class AliAPITest {

    public void ocrTest() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "app_id",
                "your private_key", "json", "GBK", "alipay_public_key", "RSA2");
        AftAifinFireeyeOcrImageQueryRequest request = new AftAifinFireeyeOcrImageQueryRequest();
        request.setBizContent("{" + "\"product_instance_id\":\"730411011\"," + "\"image\":\"-\","
                + "\"ocr_type\":\"idcard\"" + "  }");
        AftAifinFireeyeOcrImageQueryResponse response;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
