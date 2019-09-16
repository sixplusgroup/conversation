package finley.gmair.service;

import finley.gmair.model.express.Express;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {
    String post(Map<String, String> params);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Express express);

    ResultData create(Express express);
}
