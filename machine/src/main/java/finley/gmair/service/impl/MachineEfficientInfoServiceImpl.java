package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.MachineEfficientInformationDao;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.model.machine.MachineEfficientInformation;
import finley.gmair.service.AirqualityAgent;
import finley.gmair.service.DataAnalysisAgent;
import finley.gmair.service.MachineDefaultLocationService;
import finley.gmair.service.MachineEfficientInfoService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/9/16 10:00
 * @description: MachineEfficientInfoServiceImpl
 */

@Service
public class MachineEfficientInfoServiceImpl implements MachineEfficientInfoService {

    private static final long LAST_N_DAY = new Date().getTime() / (1000 * 3600 * 24);

    private static final String STATUS_TYPE_POWER = "power", POWER_ON_MIN = "powerOnMinute";

    @Autowired
    private DataAnalysisAgent dataAnalysisAgent;

    @Override
    public ResultData getSubSti(String qrcode) {
        return null;
    }

    @Autowired
    private MachineDefaultLocationService machineDefaultLocationService;

    @Autowired
    private AirqualityAgent airqualityAgent;

    @Autowired
    private MachineEfficientInformationDao machineEfficientInformationDao;

    @Override
    public ResultData getRunning(String qrcode) {
        ResultData res = new ResultData();
        long powerOnTime = 0;

        //TODO 如何检测qrcode正确性？

        ResultData response =
                dataAnalysisAgent.fetchLastNDayData(qrcode, (int) LAST_N_DAY, STATUS_TYPE_POWER);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            JSONArray dataList = JSON.parseArray(JSON.toJSONString(response.getData()));
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject one = dataList.getJSONObject(i);
                if (one.containsKey(POWER_ON_MIN))
                    powerOnTime += (Integer) one.get(POWER_ON_MIN);
            }
        }
        else {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch last n day data failed!");
            return res;
        }
        // minute -> hour
        res.setData(powerOnTime / 60);
        return res;
    }

    @Override
    public ResultData getAbnormal(String qrcode) {
        int lastNday = 0;
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineDefaultLocationService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can't find cityId by qrcode");
            return result;
        }
        String cityId = ((List<MachineDefaultLocation>) response.getData()).get(0).getCityId();
        //去null
        if (cityId == null || "null".equals(cityId)){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can't find cityId by qrcode");
            return result;
        }

        Date now = new Date();
        Date lastConfirmDate = getLastConfirmDate(qrcode);
        if (lastConfirmDate != null){
            lastNday = (int) ((now.getTime() - lastConfirmDate.getTime()) / (1000 * 60 * 60 * 24));
        }

        //得到上次
        response = airqualityAgent.fetchLastNDayData(cityId, lastNday);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can't find cityId by qrcode");
            return result;
        }
        return
    }

    //得到上次的confirm时间
    public Date getLastConfirmDate(String qrcode){
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineEfficientInformationDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return null;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            return null;
        }
        Date lastConfirmDate = ((List<MachineEfficientInformation>) response.getData()).get(0).getLastConfirmTime();
        return lastConfirmDate;
    }
}
