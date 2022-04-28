package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by sunshine on 4/8/16.
 */
public class ResultData {
    private ResponseCode responseCode;
    private Object data;
    private String description;

    public ResultData() {
        this.responseCode = ResponseCode.RESPONSE_OK;
    }

    public static ResultData ok(Object data) {
        ResultData result = new ResultData();
        result.data = data;
        return result;
    }

    public static ResultData ok(Object data, String description) {
        ResultData result = ResultData.ok(data);
        result.description = description;
        return result;
    }

    public static ResultData empty(String description) {
        ResultData result = new ResultData();
        result.setResponseCode(ResponseCode.RESPONSE_NULL);
        result.setDescription(description);
        return result;
    }

    public static ResultData error(String description) {
        ResultData result = new ResultData();
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription(description);
        return result;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
