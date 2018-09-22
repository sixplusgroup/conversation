package finley.gmair.dao.impl;

<<<<<<< Updated upstream
import finley.gmair.dao.GoodsDao;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDaoImpl implements GoodsDao {
=======
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.GoodsDao;
import finley.gmair.model.drift.Goods;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class GoodsDaoImpl extends BaseDao implements GoodsDao {
    @Override
    public ResultData queryGoods(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData insertGoods(Goods goods) {
        return null;
    }

    @Override
    public ResultData updateGoods(Goods goods) {
        return null;
    }
}
