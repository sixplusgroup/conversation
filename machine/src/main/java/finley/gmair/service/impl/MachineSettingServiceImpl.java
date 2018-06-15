package finley.gmair.service.impl;

import finley.gmair.dao.MachineSettingDao;
import finley.gmair.model.machine.v2.LightSetting;
import finley.gmair.model.machine.v2.MachineSetting;
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
        ResultData result = new ResultData();
        ResultData response = machineSettingDao.queryMachineSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No machine setting found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve machine setting");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createMachineSetting(MachineSetting setting) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", setting.getCodeValue());
        ResultData response = machineSettingDao.queryMachineSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Machine setting already exist");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query error, please try again");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            response = machineSettingDao.insertMachineSetting(setting);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to store machine setting");
            }
        }
        return result;
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

    @Override
    public ResultData fetchLightSetting(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineSettingDao.selectLightSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No light setting found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve the light setting");
        }
        return result;
    }

    @Override
    public ResultData createLightSetting(LightSetting setting) {
        ResultData result = new ResultData();
        ResultData response = machineSettingDao.insertLightSetting(setting);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store light setting to database");
        return result;
    }

    @Override
    public ResultData modifyLightSetting(LightSetting setting) {
        ResultData result = new ResultData();
        ResultData response = machineSettingDao.updateLightSetting(setting);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to update light setting");
        return result;
    }
}
