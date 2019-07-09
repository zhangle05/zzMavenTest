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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.imageio.ImageIO;

/**
 * @author zhangle
 *
 */
public class Graphics2DTest {

    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

    /**
     * @param args
     */
    public static void main(String[] args) {
        Graphics2DTest g2dt = new Graphics2DTest();
        // BufferedImage img = g2dt.generateCertImage("E", "1234567", "测试");
        // BufferedImage img =
        // g2dt.generateCertImage("12345678","张文海","513401198310040444","70",
        // System.currentTimeMillis() - 3600 * 1000 * 24l);
        // BufferedImage img = g2dt.generateSelectedCertImage("张文海",
        // "513401198310040444", "立涛园", "1", "2", "808",
        // System.currentTimeMillis() - 3600 * 1000 * 24l);
        BufferedImage img = g2dt.generateCertImage("F", 0, "513401198310040444", "張文海", "2018年12月1日", "2020年12月1日");
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
        g2d.rotate(-Math.PI / 4);
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
        g2d.rotate(Math.PI / 4);
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

    private BufferedImage generateCertImage(String sn, String name, String id, String area, long time) {
        int width = 1185, height = 1207;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // draw background
        try {
            File png = new File("D:\\Documents\\zuzhibu\\人才房\\网站文档\\approved-cert.png");
            Image img = ImageIO.read(png);
            g2d.drawImage(img, 0, 0, width, height, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        // draw main content
        Font normalFont = new Font("Open Sans, Lucida Sans", Font.PLAIN, 25);
        g2d.setFont(normalFont);
        g2d.setColor(Color.BLACK);
        // draw application info
        g2d.drawString(sn, 807, 68);
        g2d.drawString(name, 187, 383);
        g2d.drawString(id, 597, 383);
        g2d.drawString(area, 960, 445);
        // g2d.drawString(building, 380, 550);
        // g2d.drawString(tower, 493, 550);
        // g2d.drawString(room, 607, 550);
        // draw date
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(new java.util.Date(time));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        g2d.drawString(String.valueOf(year), 530, 1126);
        g2d.drawString(String.valueOf(month), 641, 1126);
        g2d.drawString(String.valueOf(day), 721, 1126);

        try {
            File png = new File("D:\\Documents\\zuzhibu\\人才房\\需求文档\\stamp.png");
            Image img = ImageIO.read(png);
            // rotation will make the stamp fuzzy
            // double theta = Math.random();
            // g2d.rotate(theta, 925, 125);
            g2d.drawImage(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH), 549, 919, 200, 200, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return result;
    }

    private BufferedImage generateSelectedCertImage(String name, String id, String community, String building,
            String tower, String room, long time) {
        int width = 1183, height = 929;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // draw background
        InputStream stampInput = null;
        try {
            File png = new File("D:\\Documents\\zuzhibu\\人才房\\网站文档\\selected-cert.png");
            Image img = ImageIO.read(png);
            g2d.drawImage(img, 0, 0, width, height, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (stampInput != null) {
                try {
                    stampInput.close();
                } catch (Exception ignore) {
                }
            }
        }
        // draw main content
        Font normalFont = new Font("Open Sans, Lucida Sans", Font.PLAIN, 25);
        g2d.setFont(normalFont);
        g2d.setColor(Color.BLACK);
        // draw application info
        g2d.drawString(name, 187, 191);
        g2d.drawString(id, 597, 191);
        g2d.drawString(community, 870, 252);
        g2d.drawString(building, 270, 316);
        g2d.drawString(tower, 383, 316);
        g2d.drawString(room, 497, 316);
        // draw date
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(new java.util.Date(time));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        g2d.drawString(String.valueOf(year), 735, 876);
        g2d.drawString(String.valueOf(month), 850, 876);
        g2d.drawString(String.valueOf(day), 925, 876);

        try {
            File png = new File("D:\\Documents\\zuzhibu\\人才房\\网站文档\\stamp.png");
            Image img = ImageIO.read(png);
            // rotation will make the stamp fuzzy
            // double theta = Math.random();
            // g2d.rotate(theta, 925, 125);
            g2d.drawImage(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH), 750, 690, 200, 200, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        return result;
    }

    private BufferedImage generateCertImage(String highest, int idType, String id, String name, String confirmTimeStr,
            String expireTimeStr) {
        int width = 1080, height = 500;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // draw background
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        g2d.rotate(-Math.PI / 4);
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
        g2d.rotate(Math.PI / 4);
        g2d.setColor(Color.BLACK);
        g2d.drawString("第1页 / 共1页", 490, 20);
        g2d.drawString("依 " + name + " 的申请，经查询萧山区高层次人才管理系统，结果如下：", 100, 120);
        // boundary
        g2d.drawRect(40, 140, 1000, 210);
        // horizontal lines
        g2d.drawLine(40, 220, 1040, 220);
        g2d.drawLine(480, 180, 1040, 180);
        g2d.drawLine(480, 260, 1040, 260);
        g2d.drawLine(40, 300, 1040, 300);
        // vertical lines
        g2d.drawLine(160, 140, 160, 350);
        g2d.drawLine(280, 140, 280, 220);
        g2d.drawLine(480, 140, 480, 300);
        g2d.drawLine(650, 140, 650, 300);
        // line 1
        g2d.drawString("被查询人", 70, 185);
        g2d.drawString("姓名", 200, 185);
        g2d.drawString(name, 300, 185);
        g2d.drawString("证件类型", 530, 165);
        g2d.drawString(this.getIdTypeName(idType), 670, 165);
        g2d.drawString("证件号码", 530, 205);
        g2d.drawString(id, 670, 205);
        // line 2
        g2d.drawString("查询结果", 70, 270);
        g2d.drawString(highest + "类人才", 200, 270);
        g2d.drawString("认定时间", 530, 250);
        g2d.drawString(confirmTimeStr, 670, 250);
        g2d.drawString("过期时间", 530, 290);
        g2d.drawString(expireTimeStr, 670, 290);
        // line 3
        g2d.drawString("查询时间", 70, 330);
        DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String time = DATE_TIME_FORMAT.format(new java.util.Date());
        g2d.drawString(time, 200, 330);
        // title
        g2d.setFont(new Font("Open Sans, Lucida Sans", Font.PLAIN, 32));
        g2d.drawString("人才认定结果查询", 400, 60);

        // InputStream stampInput = null;
        // try {
        // stampInput = getClass().getResourceAsStream("/stamp-rd.png");
        // Image img = ImageIO.read(stampInput);
        // // rotation will make the stamp fuzzy
        //// double theta = Math.random();
        //// g2d.rotate(theta, 925, 125);
        // g2d.drawImage(img, 850, 50, 150, 150, null);
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // } finally {
        // if (stampInput != null) {
        // try {
        // stampInput.close();
        // } catch (Exception ignore) {}
        // }
        // }

        return result;
    }

    private String getIdTypeName(String idType) {
        switch (idType) {
        case "0":
            return "中华人民共和国居民身份证";
        case "1":
            return "中华人民共和国永久居留证";
        case "2":
            return "红卡(浙江省海外高层次人才居住证)";
        case "3":
            return "军官证";
        case "4":
            return "外国专家证";
        case "5":
            return "外国人就业证";
        case "6":
            return "台港澳人员就业证";
        case "98":
            return "中华人民共和国护照";
        case "99":
            return "外国护照";
        }
        return "中华人民共和国居民身份证";
    }

    private String getIdTypeName(int idType) {
        return getIdTypeName(String.valueOf(idType));
    }

}
