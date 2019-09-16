package finley.gmair.service;

import finley.gmair.model.drift.Express;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {
    ResultData fetchExpress(Map<String, Object> condition);

    ResultData createExpress(Express express);

    String post(Map<String, String> params);
}
