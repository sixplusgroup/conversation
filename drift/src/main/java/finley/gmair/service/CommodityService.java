package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface CommodityService {
    ResultData fetchCommodity(Map<String, Object> condition);
}
