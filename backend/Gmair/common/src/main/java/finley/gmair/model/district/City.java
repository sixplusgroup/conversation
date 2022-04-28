package finley.gmair.model.district;

import com.alibaba.fastjson.JSONObject;

public class City extends LocationEntity {
    private String cityId;

    private String cityName;

    private String cityPinyin;

    public City() {
        super();
    }

    public City(String cityId, String cityName, String cityPinyin, double longitude, double latitude) {
        super(longitude, latitude);
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityPinyin = cityPinyin;
    }

    public City(JSONObject object) {
        this(object.getString("id"), object.getString("fullname"), object.getString("pinyin"), object.getJSONObject("location").getDouble("lng"), object.getJSONObject("location").getDouble("lat"));
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