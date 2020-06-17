package finley.gmair.dao;

import finley.gmair.model.goods.GoodsModel;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
public interface GoodsModelDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(GoodsModel model);

    ResultData detail(Map<String, Object> condition);
}
