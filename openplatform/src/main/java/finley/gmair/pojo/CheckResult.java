package finley.gmair.pojo;

import lombok.Data;

/**
 * @author lyy
 * @date 2020-07-11 1:16 下午
 */
@Data
public class CheckResult {
    private ErrorCode errorCode;

    public CheckResult(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
