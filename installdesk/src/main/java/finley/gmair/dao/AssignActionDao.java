package finley.gmair.dao;

import finley.gmair.model.installation.AssignAction;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: AssignActionDao
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/15 2:43 PM
 */
public interface AssignActionDao {
    ResultData insert(AssignAction action);

    ResultData query(Map<String, Object> condition);
}
