package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class ProvinceAirQuality extends Entity {
    private String provinceId;
    private String provinceName;
    private double aqi;
    private double pm2_5;

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

    public double getAqi() {
        return aqi;
    }

    public void setAqi(double aqi) {
        this.aqi = aqi;
    }

    public double getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(double pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public ProvinceAirQuality() {
    }

    public ProvinceAirQuality(String provinceId, double aqi, double pm2_5) {
        this.provinceId = provinceId;
        this.aqi = aqi;
        this.pm2_5 = pm2_5;
    }

    public ProvinceAirQuality(String provinceId, String provinceName, double aqi, double pm2_5) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.aqi = aqi;
        this.pm2_5 = pm2_5;
    }
}
