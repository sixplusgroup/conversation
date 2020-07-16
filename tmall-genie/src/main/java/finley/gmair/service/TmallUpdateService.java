package finley.gmair.service;

import java.io.IOException;

public interface TmallUpdateService {
    /**
     * 设备列表更新通知
     * @param accessToken
     */
    void updateListNotify(String accessToken) throws IOException;
}
