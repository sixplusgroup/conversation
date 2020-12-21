package finley.gmair.vo.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Bright Chan
 * @date: 2020/12/21 14:11
 * @description: ConsumerPartInfoVo，用于用户管理界面的信息显示
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerPartInfoVo {

    private String consumerId;

    private String name;

    private String username;

    /**
     * 用户详细地址
     */
    private String address;

    private String province;

    private String city;

    /**
     * 用户所在区，可能为空
     */
    private String district;

    private String phone;
}
