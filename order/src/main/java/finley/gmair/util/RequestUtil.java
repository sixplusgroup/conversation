package finley.gmair.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class RequestUtil {
    public static MultipartFile getFile(MultipartHttpServletRequest request, String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }
        return request.getFile(filename);
    }
}
