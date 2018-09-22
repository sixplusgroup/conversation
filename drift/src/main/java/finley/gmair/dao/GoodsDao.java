package finley.gmair.dao;

import finley.gmair.model.drift.Goods;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface GoodsDao {
    ResultData queryGoods(Map<String, Object> condition);

    ResultData insertGoods(Goods goods);

    ResultData updateGoods(Goods goods);
}
