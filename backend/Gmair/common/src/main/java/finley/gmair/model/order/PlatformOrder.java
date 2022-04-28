package finley.gmair.model.order;

import java.util.List;

public class PlatformOrder extends AbstractOrder {
    private String orderNo;

    private String province;

    private String city;

    private String district;

    private double latitude;

    private double longitude;

    private String channel;

    private String description;

    private OrderStatus status;


    public PlatformOrder() {
        super();
        this.status = OrderStatus.PAYED;
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

    public PlatformOrder(List<OrderItem> list, String orderNo, String consignee, String phone, String address, String channel, String description) {
        super(list, consignee, phone, address);
        this.orderNo = orderNo;
        this.list = list;
        this.description = description;
        this.channel = channel;
        this.status = OrderStatus.PAYED;
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

    public void setLocation(String province, String city, String district) {
        this.province = province;
        this.city = city;
        this.district = district;
    }

    public void setLocation(String province, String city, String district, double latitude, double longitude) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
