package finley.gmair.scene.dto;

/**
 * @author lyy
 * @date 2020-07-05 11:18 下午
 */
public class BizException extends RuntimeException {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }
}