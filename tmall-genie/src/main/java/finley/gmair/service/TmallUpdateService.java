package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.io.IOException;

public interface TmallUpdateService {

    /**
     * 设备列表更新通知
     *
     * @param accessToken 授权token
     * @return
     * @throws IOException
     */
    ResultData updateListNotify(String accessToken) throws IOException;

    /**
     * 密码模式token换取授权码token
     *
     * @param accessToken 密码模式token
     * @return 授权码token
     */
    String getAuthorizationToken(String accessToken);
}
