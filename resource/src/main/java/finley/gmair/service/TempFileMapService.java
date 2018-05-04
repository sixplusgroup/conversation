package finley.gmair.service;

import finley.gmair.model.resource.FileMap;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TempFileMapService {
    ResultData createTempFileMap(FileMap fileMap);

    ResultData fetchTempFileMap(Map<String, Object> condition);

    ResultData deleteTempFileMap();
}
