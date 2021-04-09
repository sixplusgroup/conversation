package finley.gmair.vo;

/**
 * @ClassName: DistrictDetailVo
 * @Description: TODO
 * @Author fan
 * @Date 2020/11/23 8:39 PM
 */
public class DistrictDetailVo {
    private String districtId;

    private String districtName;

    private String cityId;

    private String cityName;

    private String cityPinyin;

    public DistrictDetailVo() {
        super();
    }

    public DistrictDetailVo(String districtId, String districtName, String cityId, String cityName, String cityPinyin) {
        this();
        this.districtId = districtId;
        this.districtName = districtName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityPinyin = cityPinyin;
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
