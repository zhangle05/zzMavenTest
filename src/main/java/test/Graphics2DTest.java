/**
 * 
 */
package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

/**
 * @author zhangle
 *
 */
public class Graphics2DTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Graphics2DTest g2dt = new Graphics2DTest();
        BufferedImage img = g2dt.generateCertImage("E", "1234567", "测试");
        String fileName = "test.png";
        g2dt.saveImage(fileName, img);
    }

    private void saveImage(String fileName, BufferedImage img) {
        try {
            File f = new File(fileName);
            FileOutputStream fos = new FileOutputStream(f);
            ImageIO.write(img, "png", fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage generateCertImage(String highest, String id, String name) {
        BufferedImage result = new BufferedImage(1080, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // draw background
        g2d.rotate(-Math.PI/4);
        g2d.setFont(new Font("Open Sans, Lucida Sans", Font.PLAIN, 40));
        String bgText = "人才认定      结果查询      人才认定      结果查询      人才认定      结果查询      人才认定      结果查询";
        int x0 = -50, y0 = 100;
        int space = 170, offset = 3;
        Color gray = new Color(220, 220, 220);
        for (int i = 0; i < 8; i++) {
            g2d.setColor(gray);
            g2d.drawString(bgText, x0 - i * space, y0 + i * space);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bgText, x0 - i * space - offset, y0 + i * space - offset);
        }
        // draw main content
        Font normalFont = new Font("Open Sans, Lucida Sans", Font.PLAIN, 16);
        g2d.setFont(normalFont);
        g2d.rotate(Math.PI/4);
        g2d.setColor(Color.BLACK);
        g2d.drawString("第1页 / 共1页", 490, 10);
        g2d.drawString("依 " + name + " 的申请，经查询金梧桐人才认定系统，结果如下：", 100, 120);
        // boundary
        g2d.drawRect(40, 140, 1000, 180);
        // horizontal lines
        g2d.drawLine(40, 220, 1040, 220);
        g2d.drawLine(480, 180, 1040, 180);
        g2d.drawLine(40, 270, 1040, 270);
        // vertical lines
        g2d.drawLine(160, 140, 160, 320);
        g2d.drawLine(280, 140, 280, 220);
        g2d.drawLine(480, 140, 480, 220);
        g2d.drawLine(650, 140, 650, 220);
        // line 1
        g2d.drawString("被查询人", 70, 185);
        g2d.drawString("姓名", 200, 185);
        g2d.drawString(name, 300, 185);
        g2d.drawString("证件类型", 530, 165);
        g2d.drawString("身份证", 670, 165);
        g2d.drawString("证件号码", 530, 205);
        g2d.drawString(id, 670, 205);
        // line 2
        g2d.drawString("查询结果", 70, 250);
        g2d.drawString(highest + "类人才", 200, 250);
        // line 3
        g2d.drawString("查询时间", 70, 300);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String time = sdf.format(new java.util.Date());
        g2d.drawString(time, 200, 300);
        // title
        g2d.setFont(new Font("Open Sans, Lucida Sans", Font.PLAIN, 32));
        g2d.drawString("人才认定结果查询", 400, 60);
        // test png image merge
        try {
            File png = new File("D:\\Documents\\Pictures\\analytics.png");
            Image img = ImageIO.read(png);
            g2d.drawImage(img, 900, 60, 100, 100, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
