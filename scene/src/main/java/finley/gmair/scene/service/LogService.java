package finley.gmair.scene.service;

import finley.gmair.util.ResultData;

/**
 * @author : Lyy
 * @create : 2021-01-14 22:13
 **/
public interface LogService {

    ResultData getUserActionLog(String uid, String qrCode, int pageIndex, int pageSize);
}