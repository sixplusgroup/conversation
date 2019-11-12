import finley.gmair.util.ImageShareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageShareApplication {
    private static Logger logger = LoggerFactory.getLogger(ImageShareApplication.class);

    public static void main(String[] args) throws IOException {
        int[] pastlist = {60, 120, 200, 30, 100, 20, 100};
//        BufferedImage bufferedImage = ImageShareUtil.share("/Users/fan/Desktop/material", "果麦新风", "南京", 2,
//                18, 80, 18);
        BufferedImage bufferedImage = ImageShareUtil.share("/Users/fan/Desktop/material", "果麦新风", "南京", 2,
                18, 80, 1000, 10, 20, "无", 10, 10, 10, 10, 10);
        File file = new File("/Users/fan/Desktop/outdoor_carbon.jpg");
        ImageIO.write(bufferedImage, "jpg", file);
    }
}
