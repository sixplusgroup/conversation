package finley.gmair.model.machine;

import java.util.Date;

import lombok.Data;

/**
 * modelId对应的耗材信息
 */
@Data
public class MapModelMaterial {
    /**
     * 主键id
     */
    private Integer mmmId;

    /**
     * 设备型号
     */
    private String modelId;

    /**
     * 耗材名称
     */
    private String materialName;

    /**
     * 耗材购买链接
     */
    private String materialLink;

    /**
     * 数据行创建时间
     */
    private Date createTime;

    /**
     * 数据行修改时间
     */
    private Date modifyTime;
}