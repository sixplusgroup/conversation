package finley.gmair.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

/**
 * @Author Joby
 * @Date  2021/10/12 10:39
 */
@Data
public class ResponseData<T> {
    private ResponseCode responseCode;
    private T data;
    private String description;
    {
        this.responseCode = ResponseCode.RESPONSE_OK;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }


    public static ResponseData<Void> ok() {
        ResponseData<Void> result = new ResponseData<>();
        return result;
    }

    public static <T> ResponseData<T> ok(T data) {
        ResponseData<T> result = new ResponseData<T>();
        result.setData(data);
        return result;
    }

    public static <T> ResponseData<T> ok(T data, String description) {
        ResponseData<T> result = ResponseData.ok(data);
        result.setDescription(description);
        return result;
    }

    public static <T> ResponseData<T> empty(String description) {
        ResponseData<T> result = new ResponseData<T>();
        result.setResponseCode(ResponseCode.RESPONSE_NULL);
        result.setDescription(description);
        return result;
    }

    public static <T> ResponseData<T> error(String description) {
        ResponseData<T> result = new ResponseData<T>();
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription(description);
        return result;
    }
    public static <T> ResponseData<T> error(T data,String description) {
        ResponseData<T> result = new ResponseData<T>();
        result.setData(data);
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription(description);
        return result;
    }
    public static <T> ResponseData<T> transform(ResponseData<?> oldResultData) {
        ResponseData<T> result = new ResponseData<>();
        result.setResponseCode(oldResultData.getResponseCode());
        result.setDescription(oldResultData.getDescription());
        return result;
    }




}

