package finley.gmair.scene.constant;

/**
 * @author lyy
 * @date 2020-07-05 11:15 下午
 */
public enum ErrorCode {
    /**
     * 结果返回状态
     */
    UNAUTHORIZED(401, "Unauthorized"),
    UNKNOWN_ERROR(500, "未知错误"),
    SUCCESS(200, "成功"),
    APP_ID_NOT_AVAILABLE(500, "检查appid合法性失败"),
    APP_ID_IS_NULL(500, "请提供正确的appid"),
    QR_CODE_IS_NULL(500, "请提供正确的qrcode"),
    QR_CODE_NOT_SUBSCRIBE(500, "未订阅该设备"),
    QR_CODE_SUBSCRIBE_FAILED(500, "查询与该设备的订阅关系失败");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}