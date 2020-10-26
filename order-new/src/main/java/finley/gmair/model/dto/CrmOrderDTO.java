package finley.gmair.model.dto;

import lombok.Data;

/**
 * @author zm
 * @date 2020/10/26 0026 11:24
 * @description 向crm系统中新增订单时的DTO类
 **/
@Data
public class CrmOrderDTO {
    /**
     * 渠道来源
     */
    private String qdly;
    /**
     * 机器型号
     */
    private String jqxh;
    /**
     * 用户姓名
     */
    private String yhxm;
    /**
     * 订单号（订单号+联系方式，确认唯一性）
     */
    private String ddh;
    /**
     * 联系方式（订单号+联系方式，确认唯一性）
     */
    private String lxfs;
    /**
     * 数量
     */
    private String sl;
    /**
     * 下单日期
     */
    private String xdrq;
    /**
     * 订单金额（实际金额）
     */
    private String ssje;
    /**
     * 地区（如：杭州）
     */
    private String dq;
    /**
     * 地址（详细地址）
     */
    private String dz;
    /**
     * 订单状态
     */
    private String billstat;
    /**
     * 消息状态（默认是0）
     */
    private String messagestat;
    /**
     * 初始化状态（默认是1）
     */
    private String initflag;

    public CrmOrderDTO() {
        this.messagestat = "0";
        this.initflag = "1";
    }
}
