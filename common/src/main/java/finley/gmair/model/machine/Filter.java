package finley.gmair.model.machine;

import lombok.Data;

/**
 * @author zm
 * @date 2020/12/23 16:29
 * @description 滤网类
 **/
@Data
public class Filter {
    /**
     * 主键id
     */
    private String filterId;
    /**
     * 滤网名称
     */
    private String name;
    /**
     * 滤网购买链接
     */
    private String link;
    /**
     * 备注
     */
    private String remarks;
}
