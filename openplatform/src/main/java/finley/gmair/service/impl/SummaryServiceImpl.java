package finley.gmair.service.impl;

import finley.gmair.dto.QrCodeParamDTO;
import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.pojo.ApiResult;
import finley.gmair.pojo.BizException;
import finley.gmair.pojo.ErrorCode;
import finley.gmair.service.CorpMachineSubsService;
import finley.gmair.service.CorpProfileService;
import finley.gmair.service.SummaryService;
import finley.gmair.service.rpc.DataAnalysisService;
import finley.gmair.service.rpc.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.ResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyy
 * @date 2020-07-05 11:33 下午
 */
@Service
public class SummaryServiceImpl implements SummaryService {

    @Resource
    private MachineService machineService;

    @Resource
    private DataAnalysisService dataAnalysisService;

    @Resource
    private CorpProfileService corpProfileService;

    @Resource
    private CorpMachineSubsService corpMachineSubsService;

    @Override
    public ApiResult getDailyPM25(String appId, String qrCode) {
        checkAvailable(appId, qrCode);
        ResultData rpcData = machineService.getDailyPM25(qrCode);
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getHourlyPower(String appId, String qrCode) {
        checkAvailable(appId, qrCode);
        ResultData rpcData = dataAnalysisService.fetchHourly(qrCode, 24, "power");
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getHourlyPower(QrCodeParamDTO qrCodeParamDTO) {
        String appId = qrCodeParamDTO.getAppid();
        List<String> qrCodeList = qrCodeParamDTO.getQrCodeList();

        List<Object> hourlyPowerList = new ArrayList<>();
        for (String qrCode : qrCodeList) {
            checkAvailable(appId, qrCode);
            ResultData rpcData = dataAnalysisService.fetchHourly(qrCode, 24, "power");
            hourlyPowerList.add(rpcData.getData());
        }
        return ResultUtil.success("批量查询成功", hourlyPowerList);
    }

    @Override
    public ApiResult getDailyPower(String appId, String qrCode) {
        checkAvailable(appId, qrCode);
        ResultData rpcData = dataAnalysisService.fetchDaily(qrCode, 7, "power");
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getDailyPower(QrCodeParamDTO qrCodeParamDTO) {
        String appId = qrCodeParamDTO.getAppid();
        List<String> qrCodeList = qrCodeParamDTO.getQrCodeList();

        List<Object> dailyPowerList = new ArrayList<>();
        for (String qrCode : qrCodeList) {
            checkAvailable(appId, qrCode);
            ResultData rpcData = dataAnalysisService.fetchDaily(qrCode, 7, "power");
            dailyPowerList.add(rpcData.getData());
        }
        return ResultUtil.success("批量查询成功", dailyPowerList);
    }

    @Override
    public ApiResult getHourlyVolume(String appId, String qrCode) {
        checkAvailable(appId, qrCode);
        ResultData rpcData = dataAnalysisService.fetchHourly(qrCode, 24, "volume");
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getHourlyVolume(QrCodeParamDTO qrCodeParamDTO) {
        String appId = qrCodeParamDTO.getAppid();
        List<String> qrCodeList = qrCodeParamDTO.getQrCodeList();

        List<Object> hourlyVolumeList = new ArrayList<>();
        for (String qrCode : qrCodeList) {
            checkAvailable(appId, qrCode);
            ResultData rpcData = dataAnalysisService.fetchHourly(qrCode, 24, "volume");
            hourlyVolumeList.add(rpcData.getData());
        }
        return ResultUtil.success("批量查询成功", hourlyVolumeList);
    }

    @Override
    public ApiResult getDailyVolume(String appId, String qrCode) {
        checkAvailable(appId, qrCode);
        ResultData rpcData = dataAnalysisService.fetchDaily(qrCode, 7, "volume");
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getDailyVolume(QrCodeParamDTO qrCodeParamDTO) {
        String appId = qrCodeParamDTO.getAppid();
        List<String> qrCodeList = qrCodeParamDTO.getQrCodeList();

        List<Object> dailyVolumeList = new ArrayList<>();
        for (String qrCode : qrCodeList) {
            checkAvailable(appId, qrCode);
            ResultData rpcData = dataAnalysisService.fetchDaily(qrCode, 7, "volume");
            dailyVolumeList.add(rpcData.getData());
        }
        return ResultUtil.success("批量查询成功", dailyVolumeList);
    }

    /**
     * 检查appid和qrcode是否可用
     */
    private void checkAvailable(String appid, String qrcode) {

        //check empty
        if (StringUtils.isEmpty(appid)) {
            throw new BizException(ErrorCode.APP_ID_IS_NULL);
        }

        if (StringUtils.isEmpty(qrcode)) {
            throw new BizException(ErrorCode.QR_CODE_IS_NULL);
        }

        //检查appid和qrcode是否存在订阅关系
        prerequisities(appid, qrcode);
    }

    /**
     * 判断appid和qrcode是否存在订阅关系
     */
    private void prerequisities(String appid, String qrcode) {
        ResultData result = new ResultData();
        //判断appid是否合法
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            throw new BizException(ErrorCode.APP_ID_NOT_AVAILABLE);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            throw new BizException(ErrorCode.APP_ID_IS_NULL);
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查该appid和qrcode是否存在订阅关系
        Map<String, Object> con = new HashMap<>();
        con.put("corpId", corpId);
        con.put("qrcode", qrcode);
        con.put("blockFlag", false);
        response = corpMachineSubsService.fetch(con);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            throw new BizException(ErrorCode.QR_CODE_SUBSCRIBE_FAILED);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            throw new BizException(ErrorCode.QR_CODE_NOT_SUBSCRIBE);
        }
    }
}
