package finley.gmair.dao;

import finley.gmair.model.dto.OrderInfo;
import finley.gmair.model.dto.TbOrderDTO;
import finley.gmair.model.ordernew.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String orderId);

    List<Order> selectByOid(Long oid);

    /**
     * 根据oid更新相应的订单字段
     */
    int updateByOidSelective(Order record);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<OrderInfo> selectOrderInfOrderByPayTime(@Param("beginTime") Date beginTime,
                                                 @Param("endTime") Date endTime);

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