package finley.gmair.scene.utils;

import finley.gmair.scene.constant.ErrorCode;

/**
 * @author lyy
 * @date 2020-07-05 11:18 下午
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -6662127638641983451L;
    private String responseCode;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.responseCode = errorCode.getResponseCode();
    }
}