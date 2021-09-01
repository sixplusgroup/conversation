package finley.gmair.scene.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : Lyy
 * @create : 2021-01-15 15:44
 **/
@Data
@TableName(value = "user_msg_push")
public class UserMsgPushDO {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // 推送用户
    @TableField(value = "consumer_id")
    private String consumerId;

    private boolean status;

    // 创建日期
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新日期
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
