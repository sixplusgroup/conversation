package finley.gmair.dao;

import finley.gmair.model.ordernew.SkuItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuItemMapper {
    int deleteByPrimaryKey(String itemId);

    int insert(SkuItem record);

    int insertSelective(SkuItem record);

    SkuItem selectByPrimaryKey(String itemId);

    int updateByPrimaryKeySelective(SkuItem record);

    int updateByPrimaryKey(SkuItem record);

    List<SkuItem> selectAll();

    /**
     * 根据num_iid和sku_id查询返回对应的machine_model（机器型号）
     */
    List<String> selectMachineModelByNumIidAndSkuId(
            @Param("numIid") String numIid, @Param("skuId") String skuId);

    /**
     * 根据num_iid查询返回对应的machine_model（机器型号）
     */
    List<String> selectMachineModelByNumIid(
            @Param("numIid") String numIid);

    List<Boolean> selectVirtualByNumIidAndSkuId(
            @Param("numIid")String numIid,@Param("skuId")String skuId);

    List<Boolean> selectVirtualByNumIid(@Param("numIid")String numIid);
}