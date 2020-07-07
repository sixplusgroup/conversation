package finley.gmair.service;

import finley.gmair.dto.QrCodeParamDTO;
import finley.gmair.pojo.ApiResult;

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

    //---------------------------运行时长查询-----------------------------//

    /**
     * 获取设备每天的运行时长（单机查询）
     */
    ApiResult getHourlyPower(String appId, String qrCode);

    /**
     * 获取设备每天的运行时长（批量查询）
     */
    ApiResult getHourlyPower(QrCodeParamDTO qrCodeParamDTO);

    /**
     * 获取设备每周的运行时长（单机查询）
     */
    ApiResult getDailyPower(String appId, String qrCode);

    /**
     * 获取设备每周的运行时长（批量查询）
     */
    ApiResult getDailyPower(QrCodeParamDTO qrCodeParamDTO);

    //---------------------------风量查询-----------------------------//

    /**
     * 获取设备每天的风量（单机查询）
     */
    ApiResult getHourlyVolume(String appId, String qrCode);

    /**
     * 获取设备每天的风量（批量查询）
     */
    ApiResult getHourlyVolume(QrCodeParamDTO qrCodeParamDTO);

    /**
     * 获取设备每周的风量（单机查询）
     */
    ApiResult getDailyVolume(String appId, String qrCode);

    /**
     * 获取设备每周的风量（批量查询）
     */
    ApiResult getDailyVolume(QrCodeParamDTO qrCodeParamDTO);
}
