package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.GoodsModelDao;
import finley.gmair.model.goods.GoodsModel;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
@Repository
public class GoodsModelDaoImpl extends BaseDao implements GoodsModelDao {
    private Logger logger = LoggerFactory.getLogger(GoodsModelDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<GoodsModel> list = sqlSession.selectList("gmair.machine.goodsmodel.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insert(GoodsModel model) {
        ResultData result = new ResultData();
        model.setModelId(IDGenerator.generate("MOD"));
        try {
            sqlSession.insert("gmair.machine.goodsmodel.insert", model);
            result.setData(model);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}