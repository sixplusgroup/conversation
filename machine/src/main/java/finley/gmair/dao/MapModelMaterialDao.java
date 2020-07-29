package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import finley.gmair.model.machine.MapModelMaterial;

public interface MapModelMaterialDao {
    int deleteByPrimaryKey(Integer mmcId);

    int insert(MapModelMaterial record);

    int insertSelective(MapModelMaterial record);

    MapModelMaterial selectByPrimaryKey(Integer mmcId);

    int updateByPrimaryKeySelective(MapModelMaterial record);

    int updateByPrimaryKey(MapModelMaterial record);

    List<MapModelMaterial> selectAllByModelId(@Param("modelId") String modelId);
}