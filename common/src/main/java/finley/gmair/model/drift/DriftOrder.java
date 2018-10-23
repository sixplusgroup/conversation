package finley.gmair.model.drift;

import java.util.List;

public class DriftOrder extends AbstractDriftOrder {
    private String orderNo;

    private String province;

    private String city;

    private String district;

    private String description;

    private String activity;

    private double realPay;

    private String excode;

    private DriftOrderStatus status;

    public DriftOrder() {
        super();
        this.status = DriftOrderStatus.APPLIED;
    }

    public DriftOrder(String orderNo) {
        this();
        this.orderNo = orderNo;
    }

    public DriftOrder(String orderNo, String province, String city, String district) {
        this(orderNo);
        this.province = province;
        this.city = city;
        this.district = district;
    }

    public DriftOrder(List<DriftOrderItem> list, String consignee, String phone, String address, String orderNo, String province, String city, String district, String description, String activity) {
        super(list, consignee, phone, address);
        this.orderNo = orderNo;
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
}
