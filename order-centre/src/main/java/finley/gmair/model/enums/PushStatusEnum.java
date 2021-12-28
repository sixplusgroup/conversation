package finley.gmair.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-18 13:39
 * @description ：
 */

@Getter
@AllArgsConstructor
public enum PushStatusEnum {
    NOT_PUSH(0, "未推送"),
    PUSHED(1, "已推送"),
    PUSHED_FAIL(2, "已推送失败");

    private int value;

    private String desc;
}
