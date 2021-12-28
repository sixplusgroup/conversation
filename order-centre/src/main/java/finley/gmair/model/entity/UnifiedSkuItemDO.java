package finley.gmair.model.entity;

import java.util.Date;

public class UnifiedSkuItemDO {
    private String itemId;

    private Integer platform;

    private String shopId;

    private String numId;

    private String skuId;

    private String channel;

    private String machineModel;

    private Boolean isFictitious;

    private String title;

    private String propertiesName;

    private Double price;

    private Date sysCreateTime;

    private Date sysUpdateTime;

    private Boolean sysBlockFlag;

    public UnifiedSkuItemDO(String itemId, Integer platform, String shopId, String numId, String skuId, String channel, String machineModel, Boolean isFictitious, String title, String propertiesName, Double price, Date sysCreateTime, Date sysUpdateTime, Boolean sysBlockFlag) {
        this.itemId = itemId;
        this.platform = platform;
        this.shopId = shopId;
        this.numId = numId;
        this.skuId = skuId;
        this.channel = channel;
        this.machineModel = machineModel;
        this.isFictitious = isFictitious;
        this.title = title;
        this.propertiesName = propertiesName;
        this.price = price;
        this.sysCreateTime = sysCreateTime;
        this.sysUpdateTime = sysUpdateTime;
        this.sysBlockFlag = sysBlockFlag;
    }

    public UnifiedSkuItemDO() {
        super();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId == null ? null : itemId.trim();
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId == null ? null : numId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel == null ? null : machineModel.trim();
    }

    public Boolean getIsFictitious() {
        return isFictitious;
    }

    public void setIsFictitious(Boolean isFictitious) {
        this.isFictitious = isFictitious;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName == null ? null : propertiesName.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getSysCreateTime() {
        return sysCreateTime;
    }

    public void setSysCreateTime(Date sysCreateTime) {
        this.sysCreateTime = sysCreateTime;
    }

    public Date getSysUpdateTime() {
        return sysUpdateTime;
    }

    public void setSysUpdateTime(Date sysUpdateTime) {
        this.sysUpdateTime = sysUpdateTime;
    }

    public Boolean getSysBlockFlag() {
        return sysBlockFlag;
    }

    public void setSysBlockFlag(Boolean sysBlockFlag) {
        this.sysBlockFlag = sysBlockFlag;
    }
}