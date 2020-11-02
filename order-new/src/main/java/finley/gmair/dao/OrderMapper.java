package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import finley.gmair.model.dto.TbOrderDTO;
import finley.gmair.model.ordernew.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectAll();

    /**
     * 将淘宝方的单个订单导入Order表中
     */
    int insertSelectiveWithTbOrder(TbOrderDTO orderDTO);

    /**
     * 根据交易主键trade_id查询并返回所有子订单
     */
    List<Order> selectAllByTradeId(@Param("tradeId") String tradeId);

    /**
     * 根据oid（子订单编号）更新订单状态
     */
    int updateStatusByOid(
            @Param("updatedStatus") String updatedStatus,
            @Param("oid") Long oid);

    /**
     * 根据order主键更新状态
     */
    int updateStatusByOrderId(
            @Param("updatedStatus") String updatedStatus,
            @Param("orderId") String orderId);
}