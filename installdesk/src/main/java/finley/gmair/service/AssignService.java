package finley.gmair.service;

import finley.gmair.model.installation.Assign;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignService {
    ResultData create(Assign assign);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

    ResultData block(String assignId);

    ResultData remove(String assignId);
}
