import com.alibaba.fastjson.JSONObject;
import finley.gmair.util.ImageShareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageShareApplication {
    private static Logger logger = LoggerFactory.getLogger(ImageShareApplication.class);

    public static void main(String[] args) throws IOException {
        int[] pastlist = {60,120,200,30,100,20,100};
        BufferedImage bufferedImage = ImageShareUtil.share("C:/Users/赵嘉慧/Desktop/Archive", "果麦新风GM420", "南京", 2,
                18, 80, 18, pastlist);
//        120, 60,"无", 80, 0.7, 32, 84, 11,
        File file = new File("C:/Users/赵嘉慧/Desktop/outdoor_carbon.jpg");
        ImageIO.write(bufferedImage, "jpg", file);
    }
}
