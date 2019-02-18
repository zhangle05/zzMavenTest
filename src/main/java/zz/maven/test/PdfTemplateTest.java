/**
 * 
 */
package zz.maven.test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author zhangle
 *
 */
public class PdfTemplateTest {

    public static void main(String[] args) {
        PdfTemplateTest ptt = new PdfTemplateTest();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sn", "12345678");
        params.put("name", "张文海");
        params.put("id", "522501198211291234");
        params.put("community", "立涛园");
        params.put("building", "1");
        params.put("tower", "2");
        params.put("room", "102");
        params.put("area", "70");
        params.put("year", "2019");
        params.put("month", "1");
        params.put("day", "30");
        ptt.fillTemplate(params);
    }

    public void fillTemplate(Map<String, String> params) {
        // 模板路径
        String templatePath = "D:\\Documents\\zuzhibu\\人才房\\人才房政策文档\\template2.pdf";
        // 生成的新文件路径
        String newPDFPath = "output.pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            // add stamp
            // 通过域获取所在页和坐标，左下角为起点
            int pageNo = form.getFieldPositions("month").get(0).page;
            Rectangle signRect = form.getFieldPositions("year").get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            // 读图片
            Image image = Image.getInstance("D:\\Documents\\zuzhibu\\人才房\\网站文档\\stamp2.png");
            // Image image = Image.getInstance(imagePath);
            // 获取操作的页面
            PdfContentByte under = stamper.getOverContent(pageNo);
            image.scaleAbsolute(100, 100);// 重新设置宽高
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);

            BaseFont bf = BaseFont.createFont("D:\\dev\\talents\\api-gateway\\fonts\\truetype\\wqy\\wqy-zenhei.ttc,0",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(bf);
            java.util.Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                System.out.println(name);
                form.setField(name, params.get(name));
            }
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();
            reader.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
