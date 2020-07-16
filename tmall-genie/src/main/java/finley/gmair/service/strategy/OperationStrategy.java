package finley.gmair.service.strategy;

import finley.gmair.util.ResultData;

public interface OperationStrategy {

    /**
     * 电源控制（开）
     * @param deviceId 设备ID
     * @return 设备响应结果
     */
    ResultData turnOn(String deviceId);

    /**
     * 电源控制（关）
     * @param deviceId 设备ID
     * @return 设备响应结果
     */
    ResultData turnOff(String deviceId);

    /**
     * 风速调整（设置/调高一档/调低一档）
     * @param deviceId 设备ID
     * @param value 当前风速档位：1~4
     * @param up 调高
     * @param down 调低
     * @return 设备响应结果
     */
    ResultData setWindSpeed(String deviceId, String value, boolean up, boolean down);

    /**
     * 设备扫风（开）
     * @param deviceId 设备ID
     * @return 设备响应结果
     */
    ResultData openSwing(String deviceId);

    /**
     * 设备扫风（关）
     * @param deviceId 设备ID
     * @return 设备响应结果
     */
    ResultData closeSwing(String deviceId);

    ResultData setMode(String deviceId, String value);
}
