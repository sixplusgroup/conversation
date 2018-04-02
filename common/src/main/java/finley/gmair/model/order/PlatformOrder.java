package finley.gmair.model.order;

import java.util.List;

public class PlatformOrder extends AbstractOrder {
    private String orderNo;

    private String province;

    private String city;

    private String district;

    private OrderChannel channel;


    public PlatformOrder() {
        super();
    }

    public PlatformOrder(String orderNo) {
        this();
        this.orderNo = orderNo;
    }

    public PlatformOrder(String orderNo, String province, String city, String district) {
        this(orderNo);
        this.province = province;
        this.city = city;
        this.district = district;
    }

    public PlatformOrder(List<OrderItem> list, String orderNo, String province, String city, String district) {
        this(orderNo, province, city, district);
        this.list = list;
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
}
