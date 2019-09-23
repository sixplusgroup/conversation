package finley.gmair.service;

import finley.gmair.model.resource.FileMap;
import finley.gmair.util.ResultData;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface TempFileMapService {
    ResultData createTempFileMap(FileMap fileMap);

    ResultData fetchTempFileMap(Map<String, Object> condition);

    ResultData deleteTempFileMap(Map<String, Object> condition);

    String transToMD5 (MultipartFile file) throws IOException;
}
