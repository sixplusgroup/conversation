package finley.gmair.service;

import finley.gmair.model.installation.ExpressOrder;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

    ResultData create(ExpressOrder express);
}
