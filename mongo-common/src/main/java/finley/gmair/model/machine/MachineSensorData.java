package finley.gmair.model.machine;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "machine_sensor_data")
public class MachineSensorData extends Entity implements Serializable {
    private String uid;

    private int pm2_5a;

    private int pm2_5b;

    private int temp;

    private int temp_out;

    private int humidity;

    public MachineSensorData() {
        super();
    }

    public MachineSensorData(String uid, int pm2_5a, int pm2_5b, int temp, int temp_out, int humidity) {
        this();
        this.uid = uid;
        this.pm2_5a = pm2_5a;
        this.pm2_5b = pm2_5b;
        this.temp = temp;
        this.temp_out = temp_out;
        this.humidity = humidity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTemp_out() {
        return temp_out;
    }

    public void setTemp_out(int temp_out) {
        this.temp_out = temp_out;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
