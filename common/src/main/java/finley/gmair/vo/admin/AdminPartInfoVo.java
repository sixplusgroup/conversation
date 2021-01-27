package finley.gmair.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/12/21 14:11
 * @description: AdminPartInfoVo，用于用户管理界面的信息显示
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPartInfoVo {

    private String adminId;

    private String email;

    private String name;

    private Date createTime;
}
