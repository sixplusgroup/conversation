package finley.gmair.service;

import finley.gmair.model.installation.AssignCode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignCodeService {
    ResultData create(AssignCode assignCode);

    ResultData fetch(Map<String, Object> condition);

}
