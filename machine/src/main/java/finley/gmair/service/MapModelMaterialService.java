package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface MapModelMaterialService {

    /**
     * @param modelId 设备型号
     * @return 返回的ResultData中的data是对应的耗材购买链接
     * @author zm
     */
    public ResultData getMaterial(String modelId);
}
