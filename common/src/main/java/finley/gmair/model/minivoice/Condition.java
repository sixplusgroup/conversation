package finley.gmair.model.minivoice;

/**
 * 天气实况 - 用于构造系统对话
 */
public class Condition {

    // 实时天气
    String condition;

    // 温度
    String temp;

    // 体感温度
    String realFeel;

    // 紫外线强度
    String uvi;

    // 湿度
    String humidity;

    // 风向
    String windDir;

    // 风级
    String windLevel;

    // 一句话提示
    String tips;

    public Condition() {
    }

    public Condition(String condition, String temp, String realFeel, String uvi,
                     String humidity, String windDir, String windLevel, String tips) {
        this.condition = condition;
        this.temp = temp;
        this.realFeel = realFeel;
        this.uvi = uvi;
        this.humidity = humidity;
        this.windDir = windDir;
        this.windLevel = windLevel;
        this.tips = tips;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getRealFeel() {
        return realFeel;
    }

    public void setRealFeel(String realFeel) {
        this.realFeel = realFeel;
    }

    public String getUvi() {
        return uvi;
    }

    public void setUvi(String uvi) {
        this.uvi = uvi;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(String windLevel) {
        this.windLevel = windLevel;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

}
