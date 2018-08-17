package finley.gmair.model.machine;

import finley.gmair.annotation.AQIData;
import finley.gmair.annotation.Command;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.util.Constant;
import finley.gmair.util.MethodUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Document(collection = "machine_v1_status")
public class MachineV1Status extends Entity {
    private String machineId;
    @AQIData(start=0x00,length=2,name=Constant.PM25)
    private int pm25;
    @AQIData(start=0x02,length=1,name=Constant.TEMPERATURE)
    private int temperature;
    @AQIData(start=0x03,length=1,name=Constant.HUMIDITY)
    private int humidity;
    @AQIData(start=0x04,length=2,name=Constant.HCHO)
    private int hcho;
    @AQIData(start=0x06,length=2,name=Constant.CO2)
    private int co2;

    @Command(id=0x06,length=2,name=Constant.VELOCITY)
    @AQIData(start=0x08,length=2,name=Constant.VELOCITY)
    private int velocity;

    @Command(id=0x04,length=1,name=Constant.POWER)
    @AQIData(start=0x0A,length=1,name=Constant.POWER)
    private int power;

    @Command(id=0x05,length=1,name=Constant.WORKMODE)
    @AQIData(start=0x0B,length=1,name=Constant.WORKMODE)
    private int workMode; // 0 自动模式, 1 手动模式, 2 睡眠模式

    @Command(id=0x07,length=1,name=Constant.UV)
    @AQIData(start=0x0C,length=1,name=Constant.UV)
    private int uv;

    @Command(id=0x08,length=1,name=Constant.HEAT)
    @AQIData(start=0x0D,length=1,name=Constant.HEAT)
    private int heat;

    @Command(id=0x0A, length=1,name=Constant.LIGHT)
    @AQIData(start=0x0E,length=1,name=Constant.LIGHT)
    private int light;

    @Command(id=0x09, name=Constant.CYCLE,length=0x01)
    @AQIData(start=0x0F,length=1,name=Constant.CYCLE)
    private int cycle;   //0 内循环, 1 外循环

    @AQIData(start=0x10,length=1,name=Constant.VOC)
    private int voc;

    @AQIData(start=0x11,length=2,name=Constant.SIGNAL)
    private int signal;

    private String ip;

    private String time;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHcho() {
        return hcho;
    }

    public void setHcho(int hcho) {
        this.hcho = hcho;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
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

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getVoc() {
        return voc;
    }

    public void setVoc(int voc) {
        this.voc = voc;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}