package finley.gmair.service;

import finley.gmair.model.goods.Goods;
import finley.gmair.model.goods.GoodsModel;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
public interface GoodsService {
    ResultData fetchGoods(Map<String, Object> condition);

    ResultData createGoods(Goods goods);

    ResultData fetchModel(Map<String, Object> condition);

    ResultData createModel(GoodsModel model);
}
