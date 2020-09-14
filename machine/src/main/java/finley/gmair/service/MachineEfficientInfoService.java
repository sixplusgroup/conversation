package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

/**
 * @author: Bright Chan
 * @date: 2020/9/14 21:42
 * @description: MachineEfficientInfoService
 */

@Service
public interface MachineEfficientInfoService {

    ResultData getRunning(String qrcode);

}
