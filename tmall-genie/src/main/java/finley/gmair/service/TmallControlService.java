package finley.gmair.service;

import finley.gmair.model.tmallGenie.AliGenieRe;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.model.tmallGenie.Payload;

public interface TmallControlService {

    /**
     * 通过天猫精灵语音控制设备的接口
     *
     * @param payload payload
     * @param header header
     * @return 控制结果
     */
    AliGenieRe control(Payload payload, Header header);

}
