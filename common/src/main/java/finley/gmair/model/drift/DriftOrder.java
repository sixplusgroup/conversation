package finley.gmair.model.drift;

import java.util.Date;

public class DriftOrder extends AbstractDriftOrder {
    private String orderNo;

    private String equipId;

    private String province;

    private String city;

    private String district;

    private String description;

    private String activity;

    private double realPay;

    private String testTarget;

    private String excode;

    private DriftOrderStatus status;

    private DriftOrderItem item;

    public DriftOrder() {
        super();
        this.status = DriftOrderStatus.APPLIED;
    }

    public DriftOrder(String orderNo) {
        this();
        this.orderNo = orderNo;
    }

    public DriftOrder(String consumerId, String equipId, String consignee, String phone, String address, String orderNo, String province,
                      String city, String district, String description, String activity, Date expectedDate, int intervalDate) {
        super(consumerId, consignee, phone, address, expectedDate, intervalDate);
        this.orderNo = orderNo;
        this.equipId = equipId;
        this.province = province;
        this.city = city;
        this.district = district;
        this.description = description;
        this.activity = activity;
        this.status = DriftOrderStatus.APPLIED;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public String getTestTarget() {
        return testTarget;
    }

    public void setTestTarget(String testTarget) {
        this.testTarget = testTarget;
    }

    public DriftOrderItem getItem() {
        return item;
    }

    public void setItem(DriftOrderItem item) {
        this.item = item;
    }
}
