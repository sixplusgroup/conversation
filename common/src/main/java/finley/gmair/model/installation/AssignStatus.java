package finley.gmair.model.installation;

import finley.gmair.model.EnumValue;

/**
 * 安装任务状态枚举类，包括待分派、已分派、处理中、已完成、已关闭、已评价、已签收（待安装）
 */
public enum AssignStatus implements EnumValue {
    TODOASSIGN(0), ASSIGNED(1), PROCESSING(2), FINISHED(3), CLOSED(4), EVALUATED(5), RECEIVED(6);

    private int value;

    AssignStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
