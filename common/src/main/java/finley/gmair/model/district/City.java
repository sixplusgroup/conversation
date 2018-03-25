package finley.gmair.model.district;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.Entity;

public class City extends Entity {
    private String cityId;

    private String cityName;

    private String cityPinyin;

    public City() {
        super();
    }

    public City(String cityId, String cityName, String cityPinyin) {
        this();
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityPinyin = cityPinyin;
    }

    public City(JSONObject object) {
        this(object.getString("id"), object.getString("name"), object.getString("pinyin"));
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }
}