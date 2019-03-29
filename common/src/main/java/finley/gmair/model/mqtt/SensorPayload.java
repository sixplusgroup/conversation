package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class SensorPayload {
    private String machineId;
    private String id;
    private Timestamp time;
    private int pm2_5;
    private int co2;
    private int temperature_in;
    private int temperature_out;
    private int humidity;

    public SensorPayload(String machineId, String id, Timestamp time, int pm2_5, int co2, int temperature_in, int temperature_out, int humidity) {
        this.machineId = machineId;
        this.id = id;
        this.time = time;
        this.pm2_5 = pm2_5;
        this.co2 = co2;
        this.temperature_in = temperature_in;
        this.temperature_out = temperature_out;
        this.humidity = humidity;
    }

    public SensorPayload(String machineId, JSONObject object) {
        this(machineId, object.getString("id"), new Timestamp(object.getLong("time")), object.getIntValue("pm2.5"),
                object.getIntValue("co2"), object.getIntValue("temp"), object.getIntValue("temp_out"),
                object.getIntValue("humidity"));
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getTemperature_in() {
        return temperature_in;
    }

    public void setTemperature_in(int temperature_in) {
        this.temperature_in = temperature_in;
    }

    public int getTemperature_out() {
        return temperature_out;
    }

    public void setTemperature_out(int temperature_out) {
        this.temperature_out = temperature_out;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

}
