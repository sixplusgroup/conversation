package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.CommodityDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.OrderCommodityVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class CommodityDaoImpl extends BaseDao implements CommodityDao{

    @Override
    public ResultData queryCommodity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderCommodityVo> commodityVoList =
                    sqlSession.selectList("gmair.order.ordercommodity.query", condition);
            if (commodityVoList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(commodityVoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
