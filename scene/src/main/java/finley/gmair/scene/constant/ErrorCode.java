package finley.gmair.scene.constant;

/**
 * @author lyy
 * @date 2020-07-05 11:15 下午
 */
public enum ErrorCode {
    /**
     * 结果返回状态
     */
    UNKNOWN_ERROR("RESPONSE_ERROR", "未知错误"),
    SCENE_NAME_DUPLICATE("RESPONSE_ERROR", "场景名重复"),
    USER_ACTION_LOG_ERROR("RESPONSE_ERROR", "用户日志获取失败");

    private final String responseCode;
    private final String description;

    ErrorCode(String responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }
}