package finley.gmair.service;

import finley.gmair.model.ordernew.TbUser;
import finley.gmair.util.ResultData;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/19 11:01
 * @description ：
 */

public interface TbUserService {
    /**
     * 获取淘宝卖家用户信息，目前只有一个
     *
     * @return
     */
    ResultData getTbUser();

    ResultData updateTbUser(TbUser tbUser);
}
