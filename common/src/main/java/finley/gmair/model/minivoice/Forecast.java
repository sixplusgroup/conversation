package finley.gmair.model.minivoice;

/**
 * 天气预报 - 用于构造系统对话
 */
public class Forecast {

    // 白天天气
    String conditionDay;

    // 夜间天气
    String conditionNight;

    // 白天温度
    String tempDay;

    // 夜间温度
    String tempNight;

    // 白天风向
    String windDirDay;

    // 夜间风向
    String windDirNight;

    // 白天风级
    String windLevelDay;

    // 夜间风级
    String windLevelNight;

    // 预报日期
    String predictDate;

    // 湿度
    String humidity;

    public Forecast() {
    }

    public Forecast(String conditionDay, String conditionNight, String tempDay, String tempNight, String windDirDay,
                    String windDirNight, String windLevelDay, String windLevelNight, String predictDate, String humidity) {
        this.conditionDay = conditionDay;
        this.conditionNight = conditionNight;
        this.tempDay = tempDay;
        this.tempNight = tempNight;
        this.windDirDay = windDirDay;
        this.windDirNight = windDirNight;
        this.windLevelDay = windLevelDay;
        this.windLevelNight = windLevelNight;
        this.predictDate = predictDate;
        this.humidity = humidity;
    }

    public String getConditionDay() {
        return conditionDay;
    }

    public void setConditionDay(String conditionDay) {
        this.conditionDay = conditionDay;
    }

    public String getConditionNight() {
        return conditionNight;
    }

    public void setConditionNight(String conditionNight) {
        this.conditionNight = conditionNight;
    }

    public String getTempDay() {
        return tempDay;
    }

    public void setTempDay(String tempDay) {
        this.tempDay = tempDay;
    }

    public String getTempNight() {
        return tempNight;
    }

    public void setTempNight(String tempNight) {
        this.tempNight = tempNight;
    }

    public String getWindDirDay() {
        return windDirDay;
    }

    public void setWindDirDay(String windDirDay) {
        this.windDirDay = windDirDay;
    }

    public String getWindDirNight() {
        return windDirNight;
    }

    public void setWindDirNight(String windDirNight) {
        this.windDirNight = windDirNight;
    }

    public String getWindLevelDay() {
        return windLevelDay;
    }

    public void setWindLevelDay(String windLevelDay) {
        this.windLevelDay = windLevelDay;
    }

    public String getWindLevelNight() {
        return windLevelNight;
    }

    public void setWindLevelNight(String windLevelNight) {
        this.windLevelNight = windLevelNight;
    }

    public String getPredictDate() {
        return predictDate;
    }

    public void setPredictDate(String predictDate) {
        this.predictDate = predictDate;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

}
