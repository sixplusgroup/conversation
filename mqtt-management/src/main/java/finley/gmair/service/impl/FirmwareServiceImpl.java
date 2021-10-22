package finley.gmair.service.impl;

import finley.gmair.dao.MqttFirmwareDao;
import finley.gmair.model.mqttManagement.Firmware;
import finley.gmair.service.FirmwareService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class FirmwareServiceImpl implements FirmwareService {

    @Resource
    private MqttFirmwareDao firmwareDao;

    @Override
    public ResultData create(Firmware firmware) {
        ResultData result = new ResultData();
        firmware.setFirmwareId(IDGenerator.generate("FWI"));
        firmwareDao.insert(firmware);
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(firmware);
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<Firmware> firmwareList = firmwareDao.query(condition);
        if (firmwareList == null || firmwareList.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No firmware found from database");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(firmwareList);
        }
        return result;
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        ResultData result = new ResultData();
        firmwareDao.update(condition);
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Success to update firmware");
        return result;
    }
}
