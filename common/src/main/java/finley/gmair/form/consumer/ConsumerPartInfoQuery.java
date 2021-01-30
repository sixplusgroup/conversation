package finley.gmair.form.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: Bright Chan
 * @date: 2020/12/21 14:46
 * @description: ConsumerPartInfoQuery，前端给出的查询条件
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerPartInfoQuery {

    /**
     * 当前页，从1开始
     * 必填
     */
    @NotNull
    @Min(1)
    private int pageIndex;

    /**
     * 页大小
     * 必填
     */
    @NotNull
    @Min(1)
    private int pageSize;

    /**
     * 用户名（昵称？）
     * 选填
     */
    private String name;

    /**
     * 用户名，和 this.name 的区别：不清楚
     * 选填
     */
    private String username;

    /**
     * 用户所在省
     * 选填
     */
    private String province;

    /**
     * 用户所在城市
     * 选填
     */
    private String city;

    /**
     * 用户所在区
     * 选填
     */
    private String district;

}
