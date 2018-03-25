package finley.gmair.model.district;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.Entity;

public class District extends Entity {
    private String districtId;

    private String districtName;

    public District() {
        super();
    }

    public District(String districtId, String districtName) {
        this();
        this.districtId = districtId;
        this.districtName = districtName;
    }

    public District(JSONObject object) {
        this(object.getString("id"), object.getString("fullname"));
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}