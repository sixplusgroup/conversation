package finley.gmair.service.impl;

import finley.gmair.dto.QrCodeParamDTO;
import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.vo.ApiResult;
import finley.gmair.dto.CheckResult;
import finley.gmair.dto.ErrorCode;
import finley.gmair.service.CorpMachineSubsService;
import finley.gmair.service.CorpProfileService;
import finley.gmair.service.SummaryService;
import finley.gmair.service.rpc.DataAnalysisService;
import finley.gmair.service.rpc.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.ResultUtil;
import finley.gmair.vo.MachineStatusVO;
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
        CheckResult checkResult = checkAvailable(appId, qrCode);
        if (checkResult.getErrorCode() != ErrorCode.SUCCESS) {
            return ResultUtil.error(checkResult.getErrorCode());
        }
        ResultData rpcData = machineService.getDailyPM25(qrCode);
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getHourly(String appId, String qrCode, String statusType) {
        ResultData rpcData = queryOne(appId, qrCode, true, statusType);
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getHourly(QrCodeParamDTO qrCodeParamDTO, String statusType) {

        String appId = qrCodeParamDTO.getAppid();
        List<String> qrCodeList = qrCodeParamDTO.getQrCodeList();

        List<MachineStatusVO> hourlyList = new ArrayList<>();
        for (String qrCode : qrCodeList) {
            CheckResult checkResult = checkAvailable(appId, qrCode);

            MachineStatusVO machineStatusVO = new MachineStatusVO();
            machineStatusVO.setQrCode(qrCode);
            if (checkResult.getErrorCode() != ErrorCode.SUCCESS) {
                machineStatusVO.setMsg(checkResult.getErrorCode().getMsg());
            } else {
                ResultData rpcData = dataAnalysisService.fetchHourly(qrCode, 24, statusType);
                machineStatusVO.setData(rpcData.getData());
            }
            hourlyList.add(machineStatusVO);
        }
        return ResultUtil.success("批量查询成功", hourlyList);
    }

    @Override
    public ApiResult getDaily(String appId, String qrCode, String statusType) {
        ResultData rpcData = queryOne(appId, qrCode, false, statusType);
        return ResultUtil.success(rpcData.getDescription(), rpcData.getData());
    }

    @Override
    public ApiResult getDaily(QrCodeParamDTO qrCodeParamDTO, String statusType) {

        String appId = qrCodeParamDTO.getAppid();
        List<String> qrCodeList = qrCodeParamDTO.getQrCodeList();

        List<MachineStatusVO> dailyList = new ArrayList<>();
        for (String qrCode : qrCodeList) {

            CheckResult checkResult = checkAvailable(appId, qrCode);

            MachineStatusVO machineStatusVO = new MachineStatusVO();
            machineStatusVO.setQrCode(qrCode);
            if (checkResult.getErrorCode() != ErrorCode.SUCCESS) {
                machineStatusVO.setMsg(checkResult.getErrorCode().getMsg());
            } else {
                ResultData rpcData = dataAnalysisService.fetchDaily(qrCode, 7, statusType);
                machineStatusVO.setData(rpcData.getData());
            }
            dailyList.add(machineStatusVO);
        }
        return ResultUtil.success("批量查询成功", dailyList);
    }

    /**
     * 单机查询
     *
     * @param hourlyOrDaily 查一天还是查一周
     * @param statusType    查询指标
     */
    private ResultData queryOne(String appId, String qrCode, boolean hourlyOrDaily, String statusType) {
        CheckResult checkResult = checkAvailable(appId, qrCode);

        ResultData rpcData = new ResultData();
        if (checkResult.getErrorCode() != ErrorCode.SUCCESS) {
            rpcData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            rpcData.setDescription(checkResult.getErrorCode().getMsg());
        }
        if (hourlyOrDaily) {
            rpcData = dataAnalysisService.fetchHourly(qrCode, 24, statusType);
        } else {
            rpcData = dataAnalysisService.fetchDaily(qrCode, 7, statusType);
        }
        return rpcData;
    }

    /**
     * 检查appid和qrcode是否可用
     */
    private CheckResult checkAvailable(String appid, String qrcode) {

        //check empty
        if (StringUtils.isEmpty(appid)) {
            return new CheckResult(ErrorCode.APP_ID_IS_NULL);
        }

        if (StringUtils.isEmpty(qrcode)) {
            return new CheckResult(ErrorCode.QR_CODE_IS_NULL);
        }

        //检查appid和qrcode是否存在订阅关系
        //判断appid是否合法
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return new CheckResult(ErrorCode.APP_ID_NOT_AVAILABLE);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            return new CheckResult(ErrorCode.APP_ID_IS_NULL);
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
            return new CheckResult(ErrorCode.QR_CODE_SUBSCRIBE_FAILED);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            return new CheckResult(ErrorCode.QR_CODE_NOT_SUBSCRIBE);
        }

        return new CheckResult(ErrorCode.SUCCESS);
    }
}
