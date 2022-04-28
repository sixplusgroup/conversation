package finley.gmair.model.air;

import finley.gmair.model.Entity;

public abstract class AirQuality extends Entity {
    private double aqi;
    private String aqiLevel;
    private String primePollution;
    private double pm2_5;
    private double pm10;
    private double co;
    private double no2;
    private double o3;
    private double so2;

    public double getAqi() {
        return aqi;
    }

    public void setAqi(double aqi) {
        this.aqi = aqi;
    }

    public String getAqiLevel() {
        return aqiLevel;
    }

    public void setAqiLevel(String aqiLevel) {
        this.aqiLevel = aqiLevel;
    }

    public String getPrimePollution() {
        return primePollution;
    }

    public void setPrimePollution(String primePollution) {
        this.primePollution = primePollution;
    }

    public double getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(double pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getNo2() {
        return no2;
    }

    public void setNo2(double no2) {
        this.no2 = no2;
    }

    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    public double getSo2() {
        return so2;
    }

    public void setSo2(double so2) {
        this.so2 = so2;
    }
}
