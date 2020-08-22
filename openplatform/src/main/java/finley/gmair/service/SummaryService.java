package finley.gmair.service;

import finley.gmair.dto.QrCodeParamDTO;
import finley.gmair.vo.ApiResult;

/**
 * @author lyy
 * @date 2020-07-05 11:33 下午
 * @apiNote 该接口提供各种统计的方法
 * appId 关联的应用id
 * qrCode 设备的二维码
 */
public interface SummaryService {

    /**
     * 获取设备每周的pm25
     */
    ApiResult getDailyPM25(String appId, String qrCode);

    /**
     * 获取设备每天的状态（单机）
     *
     * @param statusType 具体指标（风量、运行时长等）
     */
    ApiResult getHourly(String appId, String qrCode, String statusType);

    /**
     * 获取设备每天的状态（批量）
     *
     * @param statusType 具体指标（风量、运行时长等）
     */
    ApiResult getHourly(QrCodeParamDTO qrCodeParamDTO, String statusType);

    /**
     * 获取设备每周的状态（单机）
     *
     * @param statusType 具体指标（风量、运行时长等）
     */
    ApiResult getDaily(String appId, String qrCode, String statusType);

    /**
     * 获取设备每周的状态（批量）
     *
     * @param statusType 具体指标（风量、运行时长等）
     */
    ApiResult getDaily(QrCodeParamDTO qrCodeParamDTO, String statusType);
}
