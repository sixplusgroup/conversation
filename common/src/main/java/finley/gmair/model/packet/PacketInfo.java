package finley.gmair.model.packet;

import finley.gmair.annotation.PacketConfig;

public class PacketInfo {
    @PacketConfig(command = 0x01, name = PacketConstant.URL_ADDRESS, length = 15)
    private String address;

    @PacketConfig(command = 0x02, name = PacketConstant.COM_PORT, length = 2)
    private int port;

    @PacketConfig(command = 0x03, name = PacketConstant.HEARTBEAT_INTERVAL, length = 1)
    private int heartbeat_interval;

    @PacketConfig(command = 0x04, name = PacketConstant.POWER_MODE, length = 1)
    private int power;

    @PacketConfig(command = 0x05, name = PacketConstant.WORK_MODE, length = 1)
    private int mode;

    @PacketConfig(command = 0x06, name = PacketConstant.FAN_SPEED, length = 2)
    private int speed;

    @PacketConfig(command = 0x07, name = PacketConstant.STERILISE_MODE, length = 1)
    private int sterilise;

    @PacketConfig(command = 0x08, name = PacketConstant.HEAT_MODE, length = 1)
    private int heat;

    @PacketConfig(command = 0x09, name = PacketConstant.CIRCULATION, length = 1)
    private int circulation;

    @PacketConfig(command = 0x0A, name = PacketConstant.LIGHT, length = 1)
    private int light;

    @PacketConfig(command = 0x0C, name = PacketConstant.CHILD_LOCK, length = 1)
    private int lock;

    @PacketConfig(command = 0x0D, name = PacketConstant.PROBE_INTERVAL, length = 1)
    private int probe_interval;

    @PacketConfig(command = 0x0E, name = PacketConstant.PARTIAL_PM2_5, length = 2)
    private int partial_pm2_5;

    @PacketConfig(command = 0x10, name = PacketConstant.VOLUME_MAP, length = 42)
    private int[] volumes;

    @PacketConfig(command = 0x0F, name = PacketConstant.SCREEN, length = 1)
    private int screen;
}