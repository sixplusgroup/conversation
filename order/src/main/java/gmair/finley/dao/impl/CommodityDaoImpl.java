package gmair.finley.dao.impl;

import finley.gmair.model.order.Commodity;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.CommodityVo;
import gmair.finley.dao.BaseDao;
import gmair.finley.dao.CommodityDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/8.
 */
@Repository
public class CommodityDaoImpl extends BaseDao implements CommodityDao {
    private Logger logger = LoggerFactory.getLogger(CommodityDaoImpl.class);

    private Object lock = new Object();

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CommodityVo> list = sqlSession.selectList("management.commodity.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(Commodity commodity) {
        ResultData result = new ResultData();
        commodity.setCommodityId(IDGenerator.generate("COM"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.commodity.insert", commodity);
                result.setData(commodity);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData update(Commodity commodity) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.update("management.commodity.update", commodity);
                result.setData(commodity);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
