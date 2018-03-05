package finley.gmair.model.consumer;

import finley.gmair.model.Entity;

public class Consumer extends Entity{
    private String consumerId;

    private String name;

    private String username;

    private String wechat;

    private Address address;

    private Phone phone;

    public Consumer() {
        super();
    }

    public Consumer(String name, String wechat) {
        this();
        this.name = name;
        this.wechat = wechat;
    }

    public Consumer(String name, String wechat, String address, String province, String city, String district, String phone) {
        this(name, wechat);
        this.address = new Address(address, province, city, district);;
        this.phone = new Phone(phone);
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
