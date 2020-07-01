package finley.gmair.service.impl;

import finley.gmair.dao.PreBindDao;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.service.PreBindService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
@Service
public class PreBindServiceImpl implements PreBindService {
    @Autowired
    private PreBindDao preBindDao;

    @Override
    public ResultData create(PreBindCode preBindCode) {
        ResultData result = new ResultData();

        //First, determine whether the QRCode or machineId already exists
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", preBindCode.getMachineId());
        condition.put("codeValue", preBindCode.getCodeValue());
        condition.put("blockFlag", false);

        ResultData response = preBindDao.queryBy2Id(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Machine or code is already used");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query errors, please try again");
            return result;
        }
        //Don't exist -> insert
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            response = preBindDao.insert(preBindCode);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to store preBind to database");
            }
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = preBindDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No preBind found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve preBind from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData deletePreBind(String bindId) {
        ResultData result = preBindDao.delete(bindId);
        return result;
    }

    @Override
    public ResultData fetchByDate(Map<String, Object> condition) {
        return preBindDao.queryByDate(condition);
    }

    /**
     * 在code_machine_bind中根据qrcode（codeValue）查询不到machine_id的处理方法
     *
     * @param response machineQrcodeBindService.fetch()查询返回的结果
     * @param result   用于返回的结果
     * @param qrcode   即code_value
     * @return 如果在pre_bind表中查询到记录就直接返回记录，如归找不到则返回失败result
     */
    @Override
    public ResultData checkMachineId(ResultData response, ResultData result, String qrcode) {
        ResponseCode responseCode = response.getResponseCode();
        if (responseCode == ResponseCode.RESPONSE_OK) {
            // 如果code_machine_bind中有记录，则直接返回
            return response;
        } else if (responseCode == ResponseCode.RESPONSE_NULL) {
            // 如果code_machine_bind中没有记录，则去pre_bind表中继续查询
            Map<String, Object> condition = new HashMap<>();
            condition.put("codeValue", qrcode);
            ResultData response2 = fetch(condition);
            if (response2.getResponseCode() == ResponseCode.RESPONSE_OK) {
                return response2;
            } else if (response2.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("未能查询到二维码对应的设备信息");
                return result;
            }
        }
        // response或者response2的responseCode任一是RESPONSE_ERROR
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("根据qrCode获取machineId失败");
        return result;
    }
}