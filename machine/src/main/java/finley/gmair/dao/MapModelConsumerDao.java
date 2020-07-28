package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import finley.gmair.model.machine.MapModelConsumer;

public interface MapModelConsumerDao {
    int deleteByPrimaryKey(Integer mmcId);

    int insert(MapModelConsumer record);

    int insertSelective(MapModelConsumer record);

    MapModelConsumer selectByPrimaryKey(Integer mmcId);

    int updateByPrimaryKeySelective(MapModelConsumer record);

    int updateByPrimaryKey(MapModelConsumer record);

    List<MapModelConsumer> selectAllByModelId(@Param("modelId") String modelId);
}