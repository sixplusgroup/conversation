package finley.gmair.service;

import finley.gmair.util.ResultData;

public interface MachineNotifyService {
    ResultData notify(String corpId, String qrcode);
}
