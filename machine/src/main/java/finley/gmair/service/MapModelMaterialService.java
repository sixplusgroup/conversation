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
     * 根据modelId返回相关联的耗材信息，并封装在ResultData中
     *
     * @param [modelId] 设备型号
     * @return finley.gmair.util.ResultData
     * @author zm
     * @date 2020/7/28 0028 15:54
     **/
    public ResultData getModelMaterialMap(String modelId);
}
