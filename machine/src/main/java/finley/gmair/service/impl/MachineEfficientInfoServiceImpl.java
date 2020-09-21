package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.MachineEfficientInformationDao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.dao.OutPm25DailyDao;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.model.machine.MachineEfficientInformation;
import finley.gmair.model.machine.OutPm25Daily;
import finley.gmair.service.AirqualityAgent;
import finley.gmair.service.DataAnalysisAgent;
import finley.gmair.service.MachineDefaultLocationService;
import finley.gmair.service.MachineEfficientInfoService;
import finley.gmair.util.CalendarUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
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

    private static final String STATUS_TYPE_POWER = "power", POWER_ON_MIN = "powerOnMinute";

    @Autowired
    private DataAnalysisAgent dataAnalysisAgent;

    @Autowired
    private MachineEfficientInformationDao machineEfficientInformationDao;

    @Autowired
    private MachineDefaultLocationService machineDefaultLocationService;

    @Autowired
    private AirqualityAgent airqualityAgent;

    @Autowired
    private OutPm25DailyDao outPm25DailyDao;

    @Autowired
    private MachineQrcodeBindDao machineQrcodeBindDao;

    @Override
    public long getSubSti(String qrcode) {
        Date lastConfirmDate = getLastConfirmDate(qrcode);
        Map<String,Object> condition = new HashMap<>(5);
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData resultData = machineQrcodeBindDao.select(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK){
            return 0;
        }
        String machineId = ((List<MachineQrcodeBindVo>)resultData.getData()).get(0).getMachineId();
        condition.clear();
        condition.put("machineId",machineId);
        condition.put("createTimeGTE",lastConfirmDate);
        condition.put("blockFlag",false);
        resultData = outPm25DailyDao.query(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK){
            return 0;
        }
        List<OutPm25Daily> outPm25DailyList = (List<OutPm25Daily>)resultData.getData();
        int result = 0;
        int sum = 0;
        for (OutPm25Daily outPm25Daily:outPm25DailyList){
            if (outPm25Daily.getAveragePm25()>=25.0){
                sum ++;
            }
            else {
                if (result < sum){
                    result = sum;
                }
                sum = 0;
            }
        }
        return result;
    }

    @Override
    public long getRunning(String qrcode) {
        long powerOnTime = 0;

        //TODO 如何检测qrcode正确性？

        ResultData response =
                dataAnalysisAgent.fetchLastNDayData(qrcode, getLastNDay(qrcode), STATUS_TYPE_POWER);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            JSONArray dataList = JSON.parseArray(JSON.toJSONString(response.getData()));
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject one = dataList.getJSONObject(i);
                if (one.containsKey(POWER_ON_MIN))
                    powerOnTime += (Integer) one.get(POWER_ON_MIN);
            }
        }
        else {
            return 0;
        }
        // minute -> hour
        return powerOnTime / 60;
    }

    @Override
    public int getAbnormal(String qrcode) {
        int lastNday = 0;
        int result = 0;
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineDefaultLocationService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return 0;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            return 0;
        }
        String cityId = ((List<MachineDefaultLocation>) response.getData()).get(0).getCityId();
        //去null
        if (cityId == null || "null".equals(cityId)){
            return 0;
        }

        Date now = new Date();
        Date lastConfirmDate = getLastConfirmDate(qrcode);
        if (lastConfirmDate != null){
            lastNday = (int) ((now.getTime() - lastConfirmDate.getTime()) / (1000 * 60 * 60 * 24));
        }

        //得到上次
        response = airqualityAgent.fetchLastNDayData(cityId, lastNday);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return 0;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            return 0;
        }

        List<Map<String,Object>> weather = (List<Map<String,Object>>)response.getData();
        for (Map<String,Object> a : weather){
            if (a.get("pm25") instanceof Double){
                if (((double)a.get("pm25"))>75.0){
                    result ++;
                }
            }
            else if (a.get("pm25") instanceof Integer){
                if (((int)a.get("pm25"))>75){
                    result ++;
                }
            }
        }
        return result;
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

    private int getLastNDay(String qrcode) {
        int res = 0;
        Date lastConfirmDate = getLastConfirmDate(qrcode);
        if (lastConfirmDate != null) {
            Date now = new Date();
            res = CalendarUtil.daysBetween(lastConfirmDate, now);
        }

        return res;
    }
}
