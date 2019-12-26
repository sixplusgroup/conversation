package finley.gmair.model.machine;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.packet.PacketV3Info;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @ClassName: MachineStatusV3
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/11 7:53 PM
 */

@Document(collection = "machine_v3_status")
public class MachineStatusV3 extends Entity implements Serializable {
    private String uid;

    private int pm2_5a;

    private int pm2_5b;

    private int tempIndoor;

    private int tempOutdoor;

    private int humidity;

    private int co2;

    private int power;

    private int mode;

    private int volume;

    private int heat;

    private int light;

    private int childlock;

    public MachineStatusV3() {
        super();
    }

    public MachineStatusV3(String uid, JSONObject json) {
        this();
        this.uid = uid;
        if (json.containsKey(PacketV3Info.POWER)) {
            this.power = json.getInteger(PacketV3Info.POWER);
        }
        if (json.containsKey(PacketV3Info.MODE)) {
            this.mode = json.getInteger(PacketV3Info.MODE);
        }
        if (json.containsKey(PacketV3Info.PM2_5A)) {
            this.pm2_5a = json.getInteger(PacketV3Info.PM2_5A);
        }
        if (json.containsKey(PacketV3Info.PM2_5B)) {
            this.pm2_5b = json.getInteger(PacketV3Info.PM2_5B);
        }
        if (json.containsKey(PacketV3Info.SPEED)) {
            this.volume = json.getInteger(PacketV3Info.SPEED);
        }
        if (json.containsKey(PacketV3Info.CHILDLOCK)) {
            this.childlock = json.getInteger(PacketV3Info.CHILDLOCK);
        }
        if (json.containsKey(PacketV3Info.TEMPERATURE_INDOOR)) {
            this.tempIndoor = json.getInteger(PacketV3Info.TEMPERATURE_INDOOR);
        }
        if (json.containsKey(PacketV3Info.TEMPERATURE_OUTDOOR)) {
            this.tempOutdoor = json.getInteger(PacketV3Info.TEMPERATURE_OUTDOOR);
        }
        if (json.containsKey(PacketV3Info.CO2)) {
            this.co2 = json.getInteger(PacketV3Info.CO2);
        }
        if (json.containsKey(PacketV3Info.LED)) {
            int led = json.getInteger(PacketV3Info.LED);
            if (led == 0) {
                if (json.containsKey(PacketV3Info.LIGHT)) {
                    this.light = 0;
                }
            } else {
                if (json.containsKey(PacketV3Info.LIGHT)) {
                    this.light = json.getInteger(PacketV3Info.LIGHT);
                }
            }
        }
        if (json.containsKey(PacketV3Info.HUMIDITY)) {
            this.humidity = json.getInteger(PacketV3Info.HUMIDITY);
        }
        if (json.containsKey(PacketV3Info.CREATE_AT)) {
            this.createAt = json.getLongValue(PacketV3Info.CREATE_AT) * 1000;
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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

    public int getTempIndoor() {
        return tempIndoor;
    }

    public void setTempIndoor(int tempIndoor) {
        this.tempIndoor = tempIndoor;
    }

    public int getTempOutdoor() {
        return tempOutdoor;
    }

    public void setTempOutdoor(int tempOutdoor) {
        this.tempOutdoor = tempOutdoor;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getChildlock() {
        return childlock;
    }

    public void setChildlock(int childlock) {
        this.childlock = childlock;
    }
}
