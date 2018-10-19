package finley.gmair.controller;

import finley.gmair.model.district.City;
import finley.gmair.model.formaldehyde.CaseLngLat;
import finley.gmair.model.formaldehyde.CaseProfile;
import finley.gmair.model.formaldehyde.CaseStatus;
import finley.gmair.service.CaseLngLatService;
import finley.gmair.service.CaseProfileService;
import finley.gmair.service.LocationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/case/profile")
public class CaseProfileController {
    @Autowired
    private CaseProfileService caseProfileService;

    @Autowired
    private CaseLngLatService caseLngLatService;

    @Autowired
    private LocationService locationService;

    @Transactional
    @PostMapping("/create")
    public ResultData createCaseProfile(String caseHolder, String equipmentId, String checkDuration, String checkDate, String caseCityId, String caseCityName, String caseLocation, String checkTrace, String videoId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(caseHolder) || StringUtils.isEmpty(equipmentId) || StringUtils.isEmpty(checkDuration) || StringUtils.isEmpty(checkDate) || StringUtils.isEmpty(caseCityId) || StringUtils.isEmpty(caseCityName) || StringUtils.isEmpty(caseLocation) || StringUtils.isEmpty(checkTrace) || StringUtils.isEmpty(videoId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all input");
            return result;
        }
        //TODO 检查是否插入过这条记录

        //创建case
        CaseProfile caseProfile = new CaseProfile(caseHolder, equipmentId, checkDuration, Timestamp.valueOf(checkDate), caseCityId, caseCityName, caseLocation, checkTrace, CaseStatus.COMMITED, videoId);
        ResultData response = caseProfileService.create(caseProfile);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to create");
        String caseId = ((CaseProfile) response.getData()).getCaseId();

        //上传的时候还要把经纬度解析出来存下来
        ResultData locResponse = locationService.cityProfile(caseCityId);
        double longitude = -1, latitude = -1;
        if (locResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<LinkedHashMap> cityList = (ArrayList<LinkedHashMap>) locResponse.getData();
            try{
                longitude = (Double)cityList.get(0).get("longitude");
                latitude = (Double)cityList.get(0).get("latitude");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        caseLngLatService.create(new CaseLngLat(caseId, longitude, latitude));
        return result;
    }

    @GetMapping("/fetch")
    public ResultData fetchCaseProfile(String caseId, String caseHolder, String equipmentId, String checkDate, String caseCityId, String caseStatus, String videoId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(caseId))
            condition.put("caseId", caseId);
        if (!StringUtils.isEmpty(caseHolder))
            condition.put("caseHolder", caseHolder);
        if (!StringUtils.isEmpty(equipmentId))
            condition.put("euqupmentId", equipmentId);
        if (!StringUtils.isEmpty(checkDate))
            condition.put("checkDate", checkDate);
        if (!StringUtils.isEmpty(caseCityId))
            condition.put("caseCityId", caseCityId);
        if (!StringUtils.isEmpty(caseStatus))
            condition.put("caseStatus", caseStatus);
        if (!StringUtils.isEmpty(videoId))
            condition.put("videoId", videoId);
        condition.put("blockFlag", false);
        ResultData response = caseProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch");
        return result;
    }
}
