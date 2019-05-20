package finley.gmair.service;

import finley.gmair.model.installation.AssignAction;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: AssignActionService
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/15 2:54 PM
 */
public interface AssignActionService {
    ResultData create(AssignAction action);

    ResultData fetch(Map<String, Object> condition);
}
