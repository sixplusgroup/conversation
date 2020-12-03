package finley.gmair.scene.vo;

import lombok.Data;

/**
 * @author lyy
 * @date 2020-07-05 11:05 下午
 */
@Data
public class ApiResult {
    private int code;
    private String msg;
    private Object data;
}