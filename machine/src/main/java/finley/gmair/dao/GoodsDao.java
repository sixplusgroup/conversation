package finley.gmair.dao;

import finley.gmair.model.goods.Goods;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
public interface GoodsDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(Goods goods);
}
