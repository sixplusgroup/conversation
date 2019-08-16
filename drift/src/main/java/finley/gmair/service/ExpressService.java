package finley.gmair.service;

import finley.gmair.model.drift.Express;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {
    ResultData fetchExpress(Map<String, Object> condition);

    ResultData createExpress(Express express);

    ResultData modifyExpress(Map<String, Object> condition);
}
