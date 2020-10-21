package finley.gmair.dto;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lyy
 * @date 2020-06-30 10:59 下午
 */
public class MachineStatusDTO {
    String msg;
    Object data;

    public MachineStatusDTO(String msg, JSONObject data) {
        this.msg = msg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
