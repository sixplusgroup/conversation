package finley.gmair.dao;

import finley.gmair.model.resource.FileMap;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FileMapDao {
    ResultData insertFileMap(FileMap fileMap);

    ResultData queryFileMap(Map<String, Object> condition);
}
