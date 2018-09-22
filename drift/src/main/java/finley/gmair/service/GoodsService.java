package finley.gmair.service;

import finley.gmair.model.drift.Goods;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface GoodsService {
    ResultData fetchGoods(Map<String, Object> condition);

    ResultData createGoods(Goods goods);

    ResultData modifyGoods(Map<String, Object> condition);
}
