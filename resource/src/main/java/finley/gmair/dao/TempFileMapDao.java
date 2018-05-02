package finley.gmair.dao;

import finley.gmair.model.resource.FileMap;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TempFileMapDao {
    ResultData insertTempFileMap(FileMap tempFileMap);

    ResultData queryTempFileMap(Map<String, Object> condition);
}
