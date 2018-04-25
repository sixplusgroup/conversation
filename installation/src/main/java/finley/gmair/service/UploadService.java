package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResultData upload(MultipartFile file);
}
