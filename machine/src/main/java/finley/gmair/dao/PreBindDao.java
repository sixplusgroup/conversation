package finley.gmair.dao;

import finley.gmair.model.machine.PreBindCode;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
public interface PreBindDao {
    ResultData insert(PreBindCode preBindCode);

    ResultData query(Map<String, Object> condition);

    ResultData delete(String codeValue);

    ResultData queryBy2Id(Map<String, Object> condition);
}
