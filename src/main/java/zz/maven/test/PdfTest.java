/**
 * 
 */
package zz.maven.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * @author zhangle
 *
 */
public class PdfTest {

    public static void main(String[] args) {
        FileInputStream fis;
        try {
            fis = new FileInputStream("D:\\Downloads\\滴滴电子发票.pdf");
            BufferedInputStream bis = new BufferedInputStream(fis);
            PDDocument pdfDocument = PDDocument.load(bis);
            PDFTextStripper textStripper = new PDFTextStripper();

            //in extarctContent method
            textStripper.setStartPage(1);
            textStripper.setEndPage(2);
            System.out.println(textStripper.getText(pdfDocument));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidPasswordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
