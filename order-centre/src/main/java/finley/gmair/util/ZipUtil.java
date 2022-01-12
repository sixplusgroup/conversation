package finley.gmair.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author ：tsl
 * @date ：Created in 2022-01-07 21:18
 * @description ：
 */

public class ZipUtil {
    public static void extract(String filePath, String destDir) throws ZipException {
        ZipFile zipFile = new ZipFile(filePath);
        zipFile.extractAll(destDir);
    }

    public static void extract(String filePath, String password, String destDir) throws ZipException {
        ZipFile zipFile = new ZipFile(filePath);
        zipFile.setPassword(password.toCharArray());
        zipFile.extractAll(destDir);
    }

    public static InputStream getEntryInputStream(String filePath, String password, String entryName) throws IOException {
        ZipFile zipFile = new ZipFile(filePath);
        if (StringUtils.isNotEmpty(password)) {
            zipFile.setPassword(password.toCharArray());
        }
        FileHeader fileHeader = zipFile.getFileHeader(entryName);
        return zipFile.getInputStream(fileHeader);
    }
}
