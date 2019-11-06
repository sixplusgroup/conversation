package finley.gmair.service;

import finley.gmair.model.express.Express;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Express express);

    ResultData create(Express express);
}
