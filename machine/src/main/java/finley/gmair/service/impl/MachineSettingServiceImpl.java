package finley.gmair.service.impl;

import finley.gmair.dao.MachineSettingDao;
import finley.gmair.model.machine.v2.VolumeSetting;
import finley.gmair.service.MachineSettingService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class MachineSettingServiceImpl implements MachineSettingService{

    @Autowired
    private MachineSettingDao machineSettingDao;

    @Override
    public ResultData fetchPowerActionMachine() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int hour = localDateTime.getHour();
        // see current minute is close to 0 or 30
        int minute = localDateTime.getMinute() < 30 ? 0 : 30;

        Map<String, Object> condition = new HashMap<>();
        condition.put("triggerHour", hour);
        condition.put("triggerMinute", minute);

        return machineSettingDao.selectPowerSetting(condition);
    }

    @Override
    public ResultData fetchMachineSetting(Map<String, Object> condition) {
        return machineSettingDao.queryMachineSetting(condition);
    }

    @Override
    public ResultData fetchVolumeSetting(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineSettingDao.selectVolumeSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No volume setting found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve volume setting from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createVolumeSetting(VolumeSetting setting) {
        ResultData result = new ResultData();
        ResultData response = machineSettingDao.insertVolumeSetting(setting);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store volume setting to database");
        return result;
    }

    @Override
    public ResultData modifyVolumeSetting(VolumeSetting setting) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("settingId", setting.getSettingId());
        ResultData response = machineSettingDao.selectVolumeSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("No volume setting found from database");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query volume setting, please try again later");
            return result;
        }
        response = machineSettingDao.updateVolumeSetting(setting);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to update volume setting");
        return result;
    }
}
