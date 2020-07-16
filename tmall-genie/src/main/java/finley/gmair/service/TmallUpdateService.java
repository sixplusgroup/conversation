package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.io.IOException;

public interface TmallUpdateService {

    /**
     * 设备列表更新通知
     *
     * @param accessToken
     * @return
     * @throws IOException
     */
    ResultData updateListNotify(String accessToken) throws IOException;
}
