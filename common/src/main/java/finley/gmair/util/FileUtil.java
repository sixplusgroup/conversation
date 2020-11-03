package finley.gmair.util;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 16:19
 * @description: 文件工具类
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 存文件
     * @param fileParentPath 保存文件的父路径，最后需要以 '/' 结尾
     * @param filename 文件名，带后缀
     * @param content 文件内容
     * @return 保存结果，是否成功
     */
    public static boolean saveFile(String fileParentPath, String filename, byte[] content) {
        boolean res = false;

        try {
            File file = new File(fileParentPath);
            boolean flag = false;
            if (!file.exists()) flag = file.mkdirs();

            if (flag) {
                FileOutputStream outputStream = new FileOutputStream(fileParentPath + filename);
                outputStream.write(content);
                outputStream.flush();
                outputStream.close();
                res = true;
            }
        } catch (Exception e) {
            logger.error("save file " + filename + " failed: " + e.getMessage());
        }
        return res;
    }

}
