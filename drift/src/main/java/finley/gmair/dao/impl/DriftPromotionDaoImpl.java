package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.DriftPromotionDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: DriftPromotionDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/16 3:37 PM
 */
@Repository
public class DriftPromotionDaoImpl extends BaseDao implements DriftPromotionDao {
    private Logger logger = LoggerFactory.getLogger(DriftPromotionDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.drift.promotion.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
