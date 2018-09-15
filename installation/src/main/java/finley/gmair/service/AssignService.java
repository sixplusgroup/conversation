package finley.gmair.service;

import finley.gmair.model.installation.Assign;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignService {

    ResultData createAssign(Assign assign);

    ResultData fetchAssign(Map<String, Object> condition);

    ResultData fetchAssignWithDetailName(Map<String, Object> condition);

    ResultData fetchAssign3(Map<String, Object> condition);


    ResultData updateAssign(Assign assign);

    ResultData deleteAssign(String codeValue);
}
