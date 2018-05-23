package finley.gmair.dao;

import finley.gmair.model.installation.Assign;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignDao {

    ResultData insertAssign(Assign assign);

    ResultData queryAssign(Map<String, Object> condition);

    ResultData queryAssign2(Map<String, Object> condition);

    ResultData updateAssign(Assign assign);
}
