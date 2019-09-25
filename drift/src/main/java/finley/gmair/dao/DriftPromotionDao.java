package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: DriftPromotion
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/16 3:28 PM
 */
public interface DriftPromotionDao {
    ResultData query(Map<String, Object> condition);
}
