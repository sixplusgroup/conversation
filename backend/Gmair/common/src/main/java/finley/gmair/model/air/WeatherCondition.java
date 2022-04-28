package finley.gmair.model.air;

import finley.gmair.model.Entity;

import java.sql.Timestamp;
import java.util.Date;

public abstract class WeatherCondition extends Entity {

    private String condition;

    private int conditionId;

    private int humidity;

    private int icon;

    private int pressure;

    private int realFeel;

    private Timestamp sunRise;

    private Timestamp sunSet;

    private int temp;

    private String tips;

    private int uvi;

    private int vis;

    private int windDegrees;

    private String windDir;

    private int windLevel;

    private double windSpeed;

    private Timestamp updateTime;

    public WeatherCondition() {
        super();
    }

    public WeatherCondition(String condition, int conditionId, int humidity, int icon, int pressure, int realFeel, Timestamp sunRise, Timestamp sunSet, int temp, String tips, int uvi, int vis, int windDegrees, String windDir, int windLevel, double windSpeed, Timestamp updateTime) {
        this.condition = condition;
        this.conditionId = conditionId;
        this.humidity = humidity;
        this.icon = icon;
        this.pressure = pressure;
        this.realFeel = realFeel;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.temp = temp;
        this.tips = tips;
        this.uvi = uvi;
        this.vis = vis;
        this.windDegrees = windDegrees;
        this.windDir = windDir;
        this.windLevel = windLevel;
        this.windSpeed = windSpeed;
        this.updateTime = updateTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getRealFeel() {
        return realFeel;
    }

    public void setRealFeel(int realFeel) {
        this.realFeel = realFeel;
    }

    public Timestamp getSunRise() {
        return sunRise;
    }

    public void setSunRise(Timestamp sunRise) {
        this.sunRise = sunRise;
    }

    public Timestamp getSunSet() {
        return sunSet;
    }

    public void setSunSet(Timestamp sunSet) {
        this.sunSet = sunSet;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getUvi() {
        return uvi;
    }

    public void setUvi(int uvi) {
        this.uvi = uvi;
    }

    public int getVis() {
        return vis;
    }

    public void setVis(int vis) {
        this.vis = vis;
    }

    public int getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(int windDegrees) {
        this.windDegrees = windDegrees;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public int getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(int windLevel) {
        this.windLevel = windLevel;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}

