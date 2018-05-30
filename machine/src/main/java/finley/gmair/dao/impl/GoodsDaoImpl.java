package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.GoodsDao;
import finley.gmair.model.goods.Goods;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
@Repository
public class GoodsDaoImpl extends BaseDao implements GoodsDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Goods> list = sqlSession.selectList("gmair.machine.goods.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResultData insert(Goods goods) {
        ResultData result = new ResultData();
        goods.setGoodsId(IDGenerator.generate("GUO"));
        try {
            sqlSession.insert("gmair.machine.goods.insert", goods);
            result.setData(goods);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}