package finley.gmair.service;

import finley.gmair.model.installation.MachinePic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachinePicService {

    ResultData createMachinePic(MachinePic machinePic);

    ResultData fetchMachinePic(Map<String, Object> condition);

    ResultData deleteMachinePic(Map<String, Object> condition);
}
