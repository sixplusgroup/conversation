import com.alibaba.fastjson.JSONObject;
import finley.gmair.util.ImageShareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageShareApplication {
    private static Logger logger = LoggerFactory.getLogger(ImageShareApplication.class);

    public static void main(String[] args) {
        JSONObject data = new JSONObject();
        data.put("data", 0.8);
        System.out.println(data.getInteger("data"));
    }
}
