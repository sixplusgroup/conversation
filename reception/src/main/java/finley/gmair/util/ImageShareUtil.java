package finley.gmair.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class ImageShareUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageShareUtil.class);

    private final static String[] list = new String[]{"/exc_01.png", "/mod_02.png", "/sen_03.png", "/unh_04.png", "/vun_05.png", "/haz_06.png"};

    public static BufferedImage share(String base, String name, int indoorPM2_5, int temperature, int humidity, int carbon) {
        BufferedImage indoor = null, stata = null, brand = null;
        int width = 0, height = 0;
        try {
            indoor = ImageShareUtil.indoor(base, name, indoorPM2_5, temperature, humidity, carbon);
            stata = ImageIO.read(new File(base + "/stata_03.jpg"));
            brand = ImageIO.read(new File(base + "/brand_04.jpg"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage[] image = new BufferedImage[]{indoor, brand};
        for (BufferedImage item : image) {
            width = Math.max(width, item.getWidth());
            height += item.getHeight();
        }
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        int index = 0;
        for (BufferedImage item : image) {
            g.drawImage(item, 0, index, null);
            index += item.getHeight();
        }
        g.dispose();
        return result;
    }

    public static BufferedImage share(String base, String name, int indoorPM2_5, int temperature, int humidity, int carbon, int outdoorPM2_5, int aqi, String primary, int pm10, double co, double no2, double o3, double so2) {
        BufferedImage indoor = null, outdoor = null, stata = null, brand = null;
        int width = 0, height = 0;
        try {
            indoor = ImageShareUtil.indoor(base, name, indoorPM2_5, temperature, humidity, carbon);
            outdoor = ImageShareUtil.outdoor(base, outdoorPM2_5, aqi, primary, pm10, co, no2, o3, so2);
            stata = ImageIO.read(new File(base + "/stata_03.jpg"));
            brand = ImageIO.read(new File(base + "/brand_04.jpg"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage[] image = new BufferedImage[]{indoor, outdoor, brand};
        for (BufferedImage item : image) {
            width = Math.max(width, item.getWidth());
            height += item.getHeight();
        }
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        int index = 0;
        for (BufferedImage item : image) {
            g.drawImage(item, 0, index, null);
            index += item.getHeight();
        }
        g.dispose();
        return result;
    }

    public static BufferedImage indoor(String base, String name, int current, int temp, int humid, int carbon) {
        BufferedImage indoor = null, circle = null, text = null;
        try {
            indoor = ImageIO.read(new File(base + "/indoor_01.jpg"));
            circle = pickCircle(base, current, list);
            text = ImageIO.read(new File(base + "/indoor.png"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage result = new BufferedImage(indoor.getWidth(), indoor.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(indoor, 0, 0, null);
        g.drawImage(circle, 513, 615, null);
        g.drawImage(text, 950, 1050, null);
        // 绘制室内PM2.5数值
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 400));
        g.drawString(new DecimalFormat("000").format(current), 1080, 2000);
        // 绘制设备名称
        g.setFont(new Font("SansSerif", Font.PLAIN, 120));
        g.drawString(name, 1300, 250);
        // 绘制室内温度
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("00").format(temp), 630, 2850);
        // 绘制室内湿度
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("00").format(humid), 1355, 2850);
        // 绘制二氧化碳浓度
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        String carbonContent = carbon > 0 && carbon != 2000 ? String.valueOf(carbon) : "未测";
        g.drawString(carbonContent, 2080, 2850);
        g.dispose();
        return result;
    }

    public static BufferedImage outdoor(String base, int current, int aqi, String primary, int pm10, double co, double no2, double o3, double so2) {
        BufferedImage outdoor = null, circle = null, text = null;
        try {
            outdoor = ImageIO.read(new File(base + "/outdoor_02.jpg"));
            circle = pickCircle(base, current, list);
            text = ImageIO.read(new File(base + "/outdoor.png"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage result = new BufferedImage(outdoor.getWidth(), outdoor.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(outdoor, 0, 0, null);
        g.drawImage(circle, 513, 70, null);
        g.drawImage(text, 950, 505, null);
        // 绘制室外PM2.5数值
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 400));
        g.drawString(new DecimalFormat("000").format(current), 1080, 1455);
        // 绘制AQI数值
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("000").format(aqi), 775, 2410);
        // 绘制主要污染物
        g.setFont(new Font("SansSerif", Font.PLAIN, 100));
        g.drawString(primary, 1305, 2410);
        // 绘制PM10
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("000").format(pm10), 2095, 2410);
        // 绘制一氧化碳指数
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("0.0").format(co), 450, 2845);
        // 绘制二氧化氮指数
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("0.0").format(no2), 1150, 2845);
        // 绘制臭氧指数
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("0.0").format(o3), 1750, 2845);
        // 绘制二氧化硫指数
        g.setFont(new Font("SansSerif", Font.PLAIN, 150));
        g.drawString(new DecimalFormat("0.0").format(so2), 2455, 2845);
        g.dispose();
        return result;
    }

    private static BufferedImage pickCircle(String base, int current, String[] list) throws IOException {
        BufferedImage circle = null;
        if (current < 0)
            return circle;
        else if (current <= 35)
            circle = ImageIO.read(new File(base + list[0]));
        else if (current <= 75)
            circle = ImageIO.read(new File(base + list[1]));
        else if (current <= 115)
            circle = ImageIO.read(new File(base + list[2]));
        else if (current <= 150)
            circle = ImageIO.read(new File(base + list[3]));
        else if (current <= 250)
            circle = ImageIO.read(new File(base + list[4]));
        else
            circle = ImageIO.read(new File(base + list[5]));
        return circle;
    }
}
