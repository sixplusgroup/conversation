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

    ResultData deletePreBind(String bindId);

    ResultData fetchByDate(Map<String, Object> condition);

    /**
     * 在code_machine_bind中根据qrcode（codeValue）查询不到machine_id的处理方法
     *
     * @param response machineQrcodeBindService.fetch()查询返回的结果
     * @param result   用于返回的结果
     * @param qrcode   即code_value
     * @return 如果在pre_bind表中查询到记录就直接返回记录，如归找不到则返回失败result
     */
    ResultData checkMachineId(ResultData response, ResultData result, String qrcode);
}
