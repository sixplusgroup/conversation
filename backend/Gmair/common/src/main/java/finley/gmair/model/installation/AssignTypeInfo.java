package finley.gmair.model.installation;

import finley.gmair.model.Entity;

/**
 * @author: Bright Chan
 * @date: 2021/3/13 14:51
 * @description: 工单类型类
 */
public class AssignTypeInfo extends Entity {

    /**
     * 唯一标识
     */
    private String assignTypeInfoId;

    /**
     * 工单类型
     */
    private String assignType;

    /**
     * 该类型的工单对应的上传图片的最小数量
     */
    private int pictureNumLimit;

    private Boolean isNeedQrcode;

    public Boolean getNeedQrcode() {
        return isNeedQrcode;
    }

    public void setNeedQrcode(Boolean needQrcode) {
        isNeedQrcode = needQrcode;
    }

    public AssignTypeInfo() {
        super();
    }

    public AssignTypeInfo(String assignType, int pictureNumLimit) {
        super();
        this.assignType = assignType;
        this.pictureNumLimit = pictureNumLimit;
    }

    public AssignTypeInfo(String assignTypeInfoId, String assignType, int pictureNumLimit) {
        super();
        this.assignTypeInfoId = assignTypeInfoId;
        this.assignType = assignType;
        this.pictureNumLimit = pictureNumLimit;
    }

    public String getAssignTypeInfoId() {
        return assignTypeInfoId;
    }

    public void setAssignTypeInfoId(String assignTypeInfoId) {
        this.assignTypeInfoId = assignTypeInfoId;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }

    public int getPictureNumLimit() {
        return pictureNumLimit;
    }

    public void setPictureNumLimit(int pictureNumLimit) {
        this.pictureNumLimit = pictureNumLimit;
    }
}
