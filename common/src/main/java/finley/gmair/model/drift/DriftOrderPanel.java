package finley.gmair.model.drift;

import java.util.Date;
import java.util.List;

public class DriftOrderPanel extends AbstractDriftOrder {

    private String itemRealPrice;

    private String itemTotalPrice;

    private String itemPrice;

    private String expressBackCompany;

    private String expressBackNum;

    private String expressOutCompany;

    private String expressOutNum;

//    private String expressStatus;

    private String equipName;

    private String activityName;

    private String expressAddress;

    private int quantity;

    private String equipId;

    private String province;

    private String city;

    private String district;

    private String description;

    private String activityId;

    private double realPay;

    private String excode;

    private String machineOrderNo;

    private DriftOrderStatus status;

    private List<DriftOrderItem> list;

    public DriftOrderPanel() {
        super();
        this.status = DriftOrderStatus.APPLIED;
    }

    public DriftOrderPanel(String equipName,String activityName,String expressAddress,int quantity, String consumerId, String equipId, String consignee, String phone, String address, String province,
                      String city, String district, String description, String activityId, Date expectedDate, int intervalDate,String expressOutCompany,String expressOutNum,String expressBackNum,String expressBackCompany,String itemRealPrice,String itemTotalPrice,String itemPrice) {
        super(consumerId, consignee, phone, address, expectedDate, intervalDate);
        this.equipName = equipName;
        this.activityName = activityName;
        this.expressAddress = expressAddress;
        this.quantity = quantity;
        this.equipId = equipId;
        this.equipId = equipId;
        this.province = province;
        this.city = city;
        this.district = district;
        this.description = description;
        this.activityId = activityId;
        this.expressOutCompany = expressOutCompany;
        this.expressBackCompany = expressBackCompany;
        this.expressOutNum = expressOutNum;
        this.expressBackNum = expressBackNum;
        this.itemRealPrice = itemRealPrice;
        this.itemTotalPrice = itemTotalPrice;
        this.itemPrice = itemPrice;
        this.status = DriftOrderStatus.APPLIED;

    }

    public String getExpressBackCompany() {
        return expressBackCompany;
    }

    public void setExpressBackCompany(String expressBackCompany) {
        this.expressBackCompany = expressBackCompany;
    }

    public String getExpressBackNum() {
        return expressBackNum;
    }

    public void setExpressBackNum(String expressBackNum) {
        this.expressBackNum = expressBackNum;
    }

    public String getExpressOutCompany() {
        return expressOutCompany;
    }

    public void setExpressOutCompany(String expressOutCompany) {
        this.expressOutCompany = expressOutCompany;
    }

    public String getExpressOutNum() {
        return expressOutNum;
    }

    public void setExpressOutNum(String expressOutNum) {
        this.expressOutNum = expressOutNum;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemRealPrice() {
        return itemRealPrice;
    }

    public void setItemRealPrice(String itemRealPrice) {
        this.itemRealPrice = itemRealPrice;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    //    public String getExpressStatus() {
//        return expressStatus;
//    }
//
//    public void setExpressStatus(String expressStatus) {
//        this.expressStatus = expressStatus;
//    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getExpressAddress() {
        return expressAddress;
    }

    public void setExpressAddress(String expressAddress) {
        this.expressAddress = expressAddress;
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

    public String getMachineOrderNo() {
        return machineOrderNo;
    }

    public void setMachineOrderNo(String machineOrderNo) {
        this.machineOrderNo = machineOrderNo;
    }
}
