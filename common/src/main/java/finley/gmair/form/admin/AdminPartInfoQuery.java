package finley.gmair.form.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: Bright Chan
 * @date: 2020/12/21 19:02
 * @description: AdminPartInfoQuery，前端给出的查询条件
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPartInfoQuery {

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
     * 用户名
     * 选填
     */
    private String name;
}
