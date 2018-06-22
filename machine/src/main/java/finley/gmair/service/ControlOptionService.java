package finley.gmair.service;

import finley.gmair.model.machine.ControlOption;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
public interface ControlOptionService {
    ResultData createControlOption(ControlOption option);

    ResultData fetchControlOption(Map<String, Object> condition);

    ResultData createControlOptionAction(ControlOptionAction optionAction);

    ResultData fetchControlOptionAction(Map<String, Object> condition);
}
