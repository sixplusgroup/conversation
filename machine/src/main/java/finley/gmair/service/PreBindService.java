package finley.gmair.service;

import finley.gmair.model.machine.PreBindCode;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
public interface PreBindService {
    ResultData create(PreBindCode preBindCode);

    ResultData fetch(Map<String, Object> condition);

    ResultData deletePreBind(String codeValue);

    ResultData fetchByDate(Map<String, Object> condition);
}
