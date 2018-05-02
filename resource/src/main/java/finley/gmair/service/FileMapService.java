package finley.gmair.service;

import finley.gmair.model.resource.FileMap;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FileMapService {
    ResultData createFileMap(FileMap tempFileMap);

    ResultData fetchFileMap(Map<String, Object> condition);
}
