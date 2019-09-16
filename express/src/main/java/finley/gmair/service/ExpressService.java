package finley.gmair.service;

import finley.gmair.model.drift.DriftExpress;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ExpressService {
    String post(Map<String, String> params);
}
