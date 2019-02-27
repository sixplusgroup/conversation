package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author fan
 * @create_time 2019-2019/2/27 10:37 AM
 */
public interface QRCodeDao {
    ResultData queryProfile(Map<String, Object> condition);

    ResultData queryAds(Map<String, Object> condition);
}
