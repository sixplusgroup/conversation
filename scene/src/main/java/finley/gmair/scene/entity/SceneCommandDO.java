package finley.gmair.scene.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author : Lyy
 * @create : 2020-12-26 15:52
 **/
@Data
@TableName("scene_commands")
public class SceneCommandDO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long commandId;

    // 产品ID
    // 不同产品可控制的选项不同
    @TableField(value = "goods_id")
    private String goodsId;

    @TableField(value = "command_name")
    private String commandName;

    @TableField(value = "command_component")
    private String commandComponent;

    @TableField(value = "command_operation")
    private String commandOperation;

    // 控制指令描述
    @TableField(value = "command_description")
    private String commandDescription;

    // 控制指令是否可用
    private boolean enable;
}
