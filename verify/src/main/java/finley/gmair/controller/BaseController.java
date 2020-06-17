package finley.gmair.controller;

import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;

/**
 * @ClassName: BaseController
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/23 4:18 PM
 */
public class BaseController {
    protected ResultData error(ResultData result, String message) {
        return compose(result, ResponseCode.RESPONSE_ERROR, message);
    }

    protected ResultData empty(ResultData result, String message) {
        return compose(result, ResponseCode.RESPONSE_NULL, message);
    }

    protected ResultData ok(ResultData result, String message) {
        return compose(result, ResponseCode.RESPONSE_OK, message);
    }

    protected ResultData ok(ResultData result, Object data, String message) {
        return compose(result, ResponseCode.RESPONSE_OK, data, message);
    }

    private ResultData compose(ResultData result, ResponseCode responseCode, String message) {
        result.setResponseCode(responseCode);
        result.setDescription(message);
        return result;
    }

    private ResultData compose(ResultData result, ResponseCode responseCode, Object data, String message) {
        result.setResponseCode(responseCode);
        result.setData(data);
        result.setDescription(message);
        return result;
    }
}
