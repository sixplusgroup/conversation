package finley.gmair.dao;
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
     * */
    int insertSelectiveWithTbOrder(TbOrderDTO orderDTO);
}