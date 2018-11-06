package finley.gmair.dao;

import finley.gmair.model.installation.MachinePic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachinePicDao {
    ResultData insertMachinePic(MachinePic machinePic);

    ResultData queryMachinePic(Map<String, Object> condition);

    ResultData deleteMachinePic(Map<String, Object> condition);
}
