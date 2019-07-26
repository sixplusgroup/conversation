package finley.gmair.model.drift;

import java.util.Date;
import java.util.List;

public class DriftOrder extends AbstractDriftOrder {

    private String equipId;

    private String province;

    private String city;

    private String district;

    private String description;

    private String activityId;

    private double realPay;

    private String excode;

    private DriftOrderStatus status;

    private List<DriftOrderItem> list;

    public DriftOrder() {
        super();
        this.status = DriftOrderStatus.APPLIED;
    }

    public DriftOrder(String consumerId, String equipId, String consignee, String phone, String address, String province,
                      String city, String district, String description, String activityId, Date expectedDate, int intervalDate) {
        super(consumerId, consignee, phone, address, expectedDate, intervalDate);
        this.equipId = equipId;
        this.province = province;
        this.city = city;
        this.district = district;
        this.description = description;
        this.activityId = activityId;
        this.status = DriftOrderStatus.APPLIED;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRealPay() {
        return realPay;
    }

    public void setRealPay(double realPay) {
        this.realPay = realPay;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getExcode() {
        return excode;
    }

    public void setExcode(String excode) {
        this.excode = excode;
    }

    public DriftOrderStatus getStatus() {
        return status;
    }

    public void setStatus(DriftOrderStatus status) {
        this.status = status;
    }

    public List<DriftOrderItem> getList() {
        return list;
    }

    public void setList(List<DriftOrderItem> list) {
        this.list = list;
    }
}
