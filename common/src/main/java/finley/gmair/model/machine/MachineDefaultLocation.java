package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class MachineDefaultLocation extends Entity {
    private String locationId;
    private String cityId;
    private String codeValue;
    public MachineDefaultLocation(){
        super();
    }
    public MachineDefaultLocation(String cityId,String codeValue){
        this();
        this.cityId=cityId;
        this.codeValue=codeValue;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
