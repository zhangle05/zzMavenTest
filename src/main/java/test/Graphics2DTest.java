/**
 * 
 */
package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
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
        // draw background
        g2d.rotate(-Math.PI/4);
        Font normalFont = new Font("Open Sans, Lucida Sans", Font.PLAIN, 12);
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
        g2d.setFont(normalFont);
        g2d.rotate(Math.PI/4);
        g2d.setColor(Color.BLACK);
        g2d.drawString("第1页 / 共1页", 490, 10);
        g2d.drawString("依 " + name + " 的申请，经查询金梧桐人才认定系统，结果如下：", 100, 120);
        // boundary
        g2d.drawRect(40, 140, 1000, 150);
        // horizontal lines
        g2d.drawLine(40, 190, 1040, 190);
        g2d.drawLine(40, 240, 1040, 240);
        // vertical lines
        g2d.drawLine(180, 140, 180, 290);
        g2d.drawLine(300, 140, 300, 190);
        g2d.drawLine(500, 140, 500, 190);
        g2d.drawLine(650, 140, 650, 190);
        // line 1
        g2d.drawString("被查询人", 50, 170);
        g2d.drawString("姓名", 200, 170);
        g2d.drawString(name, 350, 170);
        g2d.drawString("证件号码", 550, 170);
        g2d.drawString(id, 700, 170);
        // line 2
        g2d.drawString("查询结果", 50, 220);
        g2d.drawString(highest + "类人才", 200, 220);
        // line 3
        g2d.drawString("查询时间", 50, 270);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String time = sdf.format(new java.util.Date());
        g2d.drawString(time, 200, 270);
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
