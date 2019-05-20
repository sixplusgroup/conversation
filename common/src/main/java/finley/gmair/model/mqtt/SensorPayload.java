package finley.gmair.model.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import finley.gmair.model.Entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class SensorPayload extends Entity implements Serializable {

    private String machineId;

    private String id;

    private Timestamp time;

    private int pm2_5a;

    private int pm2_5b;

    private int co2;

    private int temperature_in;

    private int temperature_out;

    private int humidity;

    public SensorPayload(String machineId, String id, Timestamp time, int pm2_5a, int pm2_5b, int co2, int temperature_in, int temperature_out, int humidity) {
        this.machineId = machineId;
        this.id = id;
        this.time = time;
        this.pm2_5a = pm2_5a;
        this.pm2_5b = pm2_5b;
        this.co2 = co2;
        this.temperature_in = temperature_in;
        this.temperature_out = temperature_out;
        this.humidity = humidity;
    }

    public SensorPayload(String machineId, JSONObject json) {
        this(machineId, json.getString("id"), new Timestamp(json.getLong("time")), json.getIntValue("pm2.5a"), json.getIntValue("pm2.5b"), json.getIntValue("co2"), json.getIntValue("temp"), json.getIntValue("temp_out"), json.getIntValue("humidity"));
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

    public int getPm2_5a() {
        return pm2_5a;
    }

    public void setPm2_5a(int pm2_5a) {
        this.pm2_5a = pm2_5a;
    }

    public int getPm2_5b() {
        return pm2_5b;
    }

    public void setPm2_5b(int pm2_5b) {
        this.pm2_5b = pm2_5b;
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

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
