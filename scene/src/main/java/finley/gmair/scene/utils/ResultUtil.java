package finley.gmair.scene.utils;

import finley.gmair.scene.constant.ErrorCode;
import finley.gmair.scene.vo.ApiResult;

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
        apiResult.setResponseCode("RESPONSE_OK");
        apiResult.setDescription(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult success(String msg) {
        return success(msg, null);
    }

    public static ApiResult error(String code, String msg) {
        ApiResult apiResult = new ApiResult();
        apiResult.setResponseCode(code);
        apiResult.setDescription(msg);
        return apiResult;
    }

    public static ApiResult error(ErrorCode codeEnum) {
        ApiResult apiResult = new ApiResult();
        apiResult.setResponseCode(codeEnum.getResponseCode());
        apiResult.setDescription(codeEnum.getDescription());
        return apiResult;
    }
}

