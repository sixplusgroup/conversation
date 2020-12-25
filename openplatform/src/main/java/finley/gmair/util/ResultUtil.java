package finley.gmair.util;

import finley.gmair.vo.ApiResult;
import finley.gmair.dto.ErrorCode;

/**
 * @author lyy
 * @date 2020-07-05 11:04 下午
 */
public class ResultUtil {
    public static ApiResult success() {
        return success(null, null);
    }

    public static ApiResult success(String msg, Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(200);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult success(String msg) {
        return success(msg, null);
    }

    public static ApiResult error(Integer code, String msg) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static ApiResult error(ErrorCode codeEnum) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(codeEnum.getCode());
        apiResult.setMsg(codeEnum.getMsg());
        return apiResult;
    }
}
