package finley.gmair.model.district;

import com.alibaba.fastjson.JSONObject;

public class Province extends LocationEntity {
    private String provinceId;

    private String provinceName;

    private String provincePinyin;

    public Province() {
        super();
    }

    public Province(String provinceId, String provinceName, String provincePinyin, double longitude, double latitude) {
        super(longitude, latitude);
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.provincePinyin = provincePinyin;
    }

    public Province(JSONObject object) {
        this(object.getString("id"), object.getString("name"), object.getString("pinyin"), object.getJSONObject("location").getDouble("lng"), object.getJSONObject("location").getDouble("lat"));
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvincePinyin() {
        return provincePinyin;
    }

    public void setProvincePinyin(String provincePinyin) {
        this.provincePinyin = provincePinyin;
    }
}