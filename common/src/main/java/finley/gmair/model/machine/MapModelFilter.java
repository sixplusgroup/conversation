package finley.gmair.model.machine;

import java.util.Date;

import lombok.Data;

/**
 * @author zm
 * @description 机器型号-滤网购买链接
 * @date 2020/12/23 14:33
 */
@Data
public class MapModelFilter {
    /**
     * 主键id
     */
    private String mmmId;

    /**
     * 机器型号ID
     */
    private String modelId;

    /**
     * 滤网主键ID
     */
    private String filterId;

    /**
     * 数据行创建时间
     */
    private Date createTime;

    /**
     * 数据行修改时间
     */
    private Date modifyTime;
}