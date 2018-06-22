package finley.gmair.service.impl;

import finley.gmair.dao.ControlOptionActionDao;
import finley.gmair.dao.ControlOptionDao;
import finley.gmair.model.machine.ControlOption;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.service.ControlOptionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
@Service
public class ControlOptionServiceImpl implements ControlOptionService {

    @Autowired
    private ControlOptionDao controlOptionDao;

    @Autowired
    private ControlOptionActionDao controlOptionActionDao;

    @Override
    public ResultData createControlOption(ControlOption option) {
        ResultData result = new ResultData();
        ResultData response = controlOptionDao.insert(option);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store control option with: " + option.toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchControlOption(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = controlOptionDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No control option found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve control option");
        }
        return result;
    }

    @Override
    public ResultData createControlOptionAction(ControlOptionAction optionAction) {
        ResultData result = new ResultData();
        ResultData response = controlOptionActionDao.insert(optionAction);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store option action with: " + optionAction.toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchControlOptionAction(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = controlOptionActionDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No option action found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve option action");
        }
        return result;
    }
}