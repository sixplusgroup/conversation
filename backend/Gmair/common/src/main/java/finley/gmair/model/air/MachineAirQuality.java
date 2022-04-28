package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class MachineAirQuality extends Entity{
    private String maId;
    private String qrcode;
    private double pm25;
    private double temperature;
    private double humidity;
    private double co2;

    public String getMaId() {
        return maId;
    }

    public void setMaId(String maId) {
        this.maId = maId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }
}
