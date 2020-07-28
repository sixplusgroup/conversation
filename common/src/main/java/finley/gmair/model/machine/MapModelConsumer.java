package finley.gmair.model.machine;

import java.util.Date;

import lombok.Data;

/**
 * modelId对应的耗材信息
 */
@Data
public class MapModelConsumer {
    /**
     * 主键id
     */
    private Integer mmcId;

    /**
     * 设备型号
     */
    private String modelId;

    /**
     * 耗材名称
     */
    private String consumerName;

    /**
     * 耗材购买链接
     */
    private String consumerLink;

    /**
     * 数据行创建时间
     */
    private Date createTime;

    /**
     * 数据行修改时间
     */
    private Date modifyTime;
}