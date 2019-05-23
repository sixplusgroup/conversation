package finley.gmair.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ImageShareUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageShareUtil.class);

    private final static String[] circlelist = new String[]{"/exc_01.jpg", "/mod_02.jpg", "/sen_03.jpg", "/unh_04.jpg", "/vun_05.jpg", "/haz_06.jpg"};

    private final static String[] lumplist = new String[]{"/lump_01.png", "/lump_02.png", "/lump_03.png", "/lump_04.png", "/lump_05.png", "/lump_06.png"};

    public static BufferedImage share(String base, String name, String city, int indoorPM2_5, int temperature, int humidity, int carbon, int[] pastlist) {
        BufferedImage indoor = null, stata = null, brand = null, past = null;
        int width = 0, height = 0;
        try {
            if(carbon <= 0) indoor = ImageShareUtil.indoor(base, name, city, indoorPM2_5, temperature, humidity);
            else indoor = ImageShareUtil.indoor(base, name, city, indoorPM2_5, temperature, humidity, carbon);
            past = ImageShareUtil.past(base, pastlist);
            stata = ImageIO.read(new File(base + "/stata_03.jpg"));
            brand = ImageIO.read(new File(base + "/brand_04.jpg"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage[] image = new BufferedImage[]{indoor, past, brand};
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

    public static BufferedImage share(String base, String name, String city, int indoorPM2_5, int temperature, int humidity, int carbon, int outdoorPM2_5, int aqi, String primary, int pm10, double co, double no2, double o3, double so2, int[] pastlist) {
        BufferedImage indoor = null, outdoor = null, stata = null, brand = null, past = null;
        int width = 0, height = 0;
        try {
            if(carbon <= 0) indoor = ImageShareUtil.indoor(base, name, city, indoorPM2_5, temperature, humidity);
            else indoor = ImageShareUtil.indoor(base, name, city, indoorPM2_5, temperature, humidity, carbon);
            outdoor = ImageShareUtil.outdoor(base, outdoorPM2_5, aqi, primary, pm10, co, no2, o3, so2);
            past = ImageShareUtil.past(base, pastlist);
            stata = ImageIO.read(new File(base + "/stata_03.jpg"));
            brand = ImageIO.read(new File(base + "/brand_04.jpg"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage[] image = new BufferedImage[]{indoor, outdoor, past, brand};
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

    public static BufferedImage indoor(String base, String name, String city, int current, int temp, int humid, int carbon) {
        BufferedImage indoor = null, circle = null, text = null, lump = null;
        Color color = null;
        try {
            indoor = ImageIO.read(new File(base + "/indoor_01.jpg"));
            Object pick = pick(base, current, circlelist, lumplist);
            color = (Color) ((Map) pick).get("color");
            lump = (BufferedImage) ((Map) pick).get("lump");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage result = new BufferedImage(indoor.getWidth(), indoor.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(indoor, 0, 0, null);
        // 绘制室内PM2.5数值和空气质量等级
        g.setColor(color);
        Font pm2_5Font = getSelfDefinedFont(base + "/EXTRALIGHT_0.TTF","pm2_5");
        g.setFont(pm2_5Font);
        g.drawString(new DecimalFormat("000").format(current), 1000, 1900);
        g.drawImage(lump, 1100, 2040, null);
        g.setColor(Color.BLACK);
        // 绘制设备名称
        Font nameFont = getSelfDefinedFont(base + "/NORMAL_0.TTF","name");
        g.setFont(nameFont);
        g.drawString(name, 1020, 320);
        //绘制城市
        g.drawString(city, 230, 590);
        // 绘制室内湿度
        Font indoorFont = getSelfDefinedFont(base + "/EXTRALIGHT_0.TTF","indoor");
        g.setFont(indoorFont);
        g.drawString(new DecimalFormat("00").format(humid), 695, 2900);
        // 绘制室内温度
        g.drawString(new DecimalFormat("00").format(temp), 1590, 2900);
        // 绘制二氧化碳浓度
        String carbonContent = carbon > 0 && carbon != 2000 ? String.valueOf(carbon) : "未测";
        g.drawString(carbonContent, 2435, 2900);
        g.dispose();
        return result;
    }

    public static BufferedImage indoor(String base, String name, String city, int current, int temp, int humid) {
        BufferedImage indoor = null, lump = null;
        Color color = null;
        try {
            indoor = ImageIO.read(new File(base + "/indoor_02.jpg"));
            Object pick = pick(base, current, circlelist, lumplist);
            color = (Color) ((Map) pick).get("color");
            lump = (BufferedImage) ((Map) pick).get("lump");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage result = new BufferedImage(indoor.getWidth(), indoor.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(indoor, 0, 0, null);
        // 绘制室内PM2.5数值和空气质量等级
        g.setColor(color);
        Font pm2_5Font = getSelfDefinedFont(base + "/EXTRALIGHT_0.TTF","pm2_5");
        g.setFont(pm2_5Font);
        g.drawString(new DecimalFormat("000").format(current), 1000, 1900);
        g.drawImage(lump, 1100, 2040, null);
        g.setColor(Color.BLACK);
        // 绘制设备名称
        Font nameFont = getSelfDefinedFont(base + "/NORMAL_0.TTF","name");
        g.setFont(nameFont);
        g.drawString(name, 1020, 320);
        //绘制城市
        g.drawString(city, 230, 590);
        // 绘制室内湿度
        Font indoorFont = getSelfDefinedFont(base + "/EXTRALIGHT_0.TTF","indoor");
        g.setFont(indoorFont);
        g.drawString(new DecimalFormat("00").format(humid), 1050, 2820);
        // 绘制室内温度
        g.drawString(new DecimalFormat("00").format(temp), 2110, 2820);
        g.dispose();
        return result;
    }

    public static BufferedImage outdoor(String base, int current, int aqi, String primary, int pm10, double co, double no2, double o3, double so2) {
        BufferedImage outdoor = null, circle = null, text = null, lump = null;
        Color color = null;
        try {
            outdoor = ImageIO.read(new File(base + "/outdoor_01.jpg"));
            Object pick = pick(base, current, circlelist, lumplist);
            color = (Color) ((Map) pick).get("color");
            lump = (BufferedImage) ((Map) pick).get("lump");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage result = new BufferedImage(outdoor.getWidth(), outdoor.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(outdoor, 0, 0, null);
        // 绘制室外PM2.5数值和空气质量等级
        g.setColor(color);
        Font pm2_5Font = getSelfDefinedFont(base + "/EXTRALIGHT_0.TTF","pm2_5");
        g.setFont(pm2_5Font);
        g.drawString(new DecimalFormat("000").format(current), 980, 1010);
        g.drawImage(lump, 1100, 1180, null);
        g.setColor(Color.BLACK);
        Font outdoorFont = getSelfDefinedFont(base + "/EXTRALIGHT_0.TTF","outdoor");
        g.setFont(outdoorFont);
        // 绘制AQI数值
        g.drawString(String.valueOf(aqi),780,1950);
        // 绘制主要污染物
        g.drawString(primary, 1450, 1950);
        // 绘制PM10
        g.drawString(String.valueOf(pm10), 2072, 1950);
        // 绘制一氧化碳指数
        g.drawString(new DecimalFormat("0.0").format(co), 400, 2390);
        // 绘制二氧化氮指数
        g.drawString(new DecimalFormat("0.0").format(no2), 1025, 2390);
        // 绘制臭氧指数
        g.drawString(new DecimalFormat("0.0").format(o3), 1710, 2390);
        // 绘制二氧化硫指数
        g.drawString(new DecimalFormat("0.0").format(so2), 2385, 2390);
        g.dispose();
        return result;
    }

    public static BufferedImage past(String base, int[] pastlist) throws IOException {
        BufferedImage past = null, circle = null;
        try {
            past = ImageIO.read(new File(base + "/past_02.jpg"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        BufferedImage result = new BufferedImage(past.getWidth(), past.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(past, 0, 0, null);
        for (int i = 0; i < pastlist.length; i++) {
            int pm2_5 = pastlist[i];
            circle = (BufferedImage) pick(base, pm2_5, circlelist, lumplist).get("circle");
            int x = 220;
            g.drawImage(circle, x + i * 400, 500, null);
        }
        g.dispose();
        return result;
    }

    private static Map<String, Object> pick(String base, int current, String[] circlelist, String[] lumplist) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        BufferedImage circle = null, lump = null;
        Color color = null;
        if (current < 0)
            return null;
        else if (current <= 35) {
            circle = ImageIO.read(new File(base + circlelist[0]));
            color = new Color(84, 180, 49);
            lump = ImageIO.read(new File(base + lumplist[0]));
        } else if (current <= 75) {
            circle = ImageIO.read(new File(base + circlelist[1]));
            color = new Color(238, 234, 56);
            lump = ImageIO.read(new File(base + lumplist[1]));
        } else if (current <= 115) {
            circle = ImageIO.read(new File(base + circlelist[2]));
            color = new Color(239, 123, 26);
            lump = ImageIO.read(new File(base + lumplist[2]));
        } else if (current <= 150) {
            circle = ImageIO.read(new File(base + circlelist[3]));
            color = new Color(231, 20, 26);
            lump = ImageIO.read(new File(base + lumplist[3]));
        } else if (current <= 250) {
            circle = ImageIO.read(new File(base + circlelist[4]));
            color = new Color(117, 12, 93);
            lump = ImageIO.read(new File(base + lumplist[4]));
        } else {
            circle = ImageIO.read(new File(base + circlelist[5]));
            color = new Color(121, 23, 37);
            lump = ImageIO.read(new File(base + lumplist[5]));
        }
        map.put("circle", circle);
        map.put("color", color);
        map.put("lump", lump);
        return map;
    }

    public static java.awt.Font getSelfDefinedFont(String filepath, String type) {
        java.awt.Font font = null;
        File file = new File(filepath);
        try {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, file);
            if(type == "pm2_5") font = font.deriveFont(java.awt.Font.PLAIN, 700);
            else if(type == "indoor") font = font.deriveFont(java.awt.Font.PLAIN, 125);
            else if (type == "outdoor") font = font.deriveFont(java.awt.Font.PLAIN, 150);
            else if(type == "name") font = font.deriveFont(java.awt.Font.PLAIN, 120);
        } catch (FontFormatException e) {
            return null;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return font;
    }
}
