package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MapModelMaterialService {

    /**
     * @param condition map中的modelId是设备型号
     * @return 返回的ResultData中的data是对应的耗材购买链接
     * @author zm
     */
    public ResultData fetch(Map<String,Object> condition);
}
