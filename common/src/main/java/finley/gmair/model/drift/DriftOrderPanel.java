package finley.gmair.model.drift;

import java.util.Date;
import java.util.List;

public class DriftOrderPanel extends AbstractDriftOrder {

    private String company;

    private String expressNum;

    private String expressStatus;

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
                      String city, String district, String description, String activityId, Date expectedDate, int intervalDate,String company) {
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
        this.company = company;
        this.status = DriftOrderStatus.APPLIED;

    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(String expressStatus) {
        this.expressStatus = expressStatus;
    }

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
