package finley.gmair.service;

import finley.gmair.model.machine.MapModelMaterial;
import finley.gmair.util.ResultData;

public interface MapModelMaterialService {

    public int deleteByPrimaryKey(Integer mmcId);

    public int insert(MapModelMaterial record);

    public int insertSelective(MapModelMaterial record);

    public MapModelMaterial selectByPrimaryKey(Integer mmcId);

    public int updateByPrimaryKeySelective(MapModelMaterial record);

    public int updateByPrimaryKey(MapModelMaterial record);

    /**
     * @param modelId 设备型号
     * @return 返回的ResultData中的data是对应的耗材购买链接
     * @author zm
     */
    public ResultData getMaterial(String modelId);
}
