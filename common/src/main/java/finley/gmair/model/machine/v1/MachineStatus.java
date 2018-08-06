package finley.gmair.model.machine.v1;

import finley.gmair.model.Entity;

public class MachineStatus extends Entity{
    private String deviceID;
    private int pm25;
    private int temperature;
    private int humidity;
    private int hcho;
    private int co2;
    private int velocity;
    private int power;
    private int workMode; // 0 自动模式, 1 手动模式, 2 睡眠模式
    private int uv;
    private int heat;
    private int light;
    private int cycle;   //0 内循环, 1 外循环
    private int voc;
    private int signal;
    private String ip;
    private String time;

}
