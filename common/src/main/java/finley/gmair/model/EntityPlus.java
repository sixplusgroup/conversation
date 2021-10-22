package finley.gmair.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author Joby
 * @Date 10/18/2021 2:16 PM
 * @Description if you use [mybatis-plus] and [logic delete blockFlag], you should extends this class
 */
@Data
public abstract class EntityPlus {
    @TableLogic
    protected Boolean blockFlag;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    public EntityPlus() {
        this.blockFlag = false;
        this.createTime = new Date();
    }


}
