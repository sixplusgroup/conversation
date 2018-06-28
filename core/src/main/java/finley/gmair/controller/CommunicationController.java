package finley.gmair.controller;

import finley.gmair.model.packet.AbstractPacketV2;
import finley.gmair.model.packet.Action;
import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.model.packet.PacketConstant;
import finley.gmair.netty.GMRepository;
import finley.gmair.util.PacketUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/core")
public class CommunicationController {

    @Autowired
    private GMRepository repository;

    /** 心跳包
     * This will issue a heartbeat packet to the target uid
     * if the uid does not exist in the cache, it will not send the packet
     *
     * @param uid
     * @return
     */
    @GetMapping("/com/heartbeat")
    public ResultData heartbeat(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        HeartBeatPacket packet = PacketUtil.generateHeartBeat(uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a heart beat packet to the target uid: ").append(uid).toString());
        return result;
    }

    /**
     * This will issue a probe packet to the target uid asking for the probe data
     * if the uid does not exist in the cache, it will not send the packet
     *
     * @param uid
     * @return
     */
    @GetMapping("/com/probe")
    public ResultData probe(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        HeartBeatPacket packet = PacketUtil.generateProbe(uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query probe) to the target uid: ").append(uid).toString());
        return result;
    }

    //设置板子的IP地址
    @PostMapping("/com/config/address")
    public ResultData configAddress(String uid, String address) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.URL_ADDRESS, address, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config server url: ").append(address).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    //查询
    @GetMapping("/com/probe/address")
    public ResultData probeAddress(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.URL_ADDRESS, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query server url) to the target uid: ").append(uid).toString());
        return result;
    }

    @PostMapping("/com/config/port")
    public ResultData configPort(String uid, int port) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.COM_PORT, port, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config server port: ").append(port).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/port")
    public ResultData probePort(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.COM_PORT, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query server port) to the target uid: ").append(uid).toString());
        return result;
    }

    @PostMapping("/com/config/heartbeat/interval")
    public ResultData configHeartbeatCycle(String uid, int interval) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.HEARTBEAT_INTERVAL, interval, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config heartbeat interval: ").append(interval).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/heartbeat/interval")
    public ResultData probeHeartbeatCycle(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.HEARTBEAT_INTERVAL, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query heartbeat interval) to the target uid: ").append(uid).toString());
        return result;
    }

    @PostMapping("/com/config/probe/interval")
    public ResultData configProbeCycle(String uid, int interval) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.PROBE_INTERVAL, interval, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config probe interval: ").append(interval).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/probe/interval")
    public ResultData probeProbeCycle(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.PROBE_INTERVAL, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query probe interval) to the target uid: ").append(uid).toString());
        return result;
    }

    @PostMapping("/com/config/power")
    public ResultData configPower(String uid, int power) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.POWER_MODE, power, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config power mode: ").append(power).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/power")
    public ResultData probePower(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.POWER_MODE, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query power mode) to the target uid: ").append(uid).toString());
        return result;
    }

    //配置主板工作模式
    @PostMapping("/com/config/mode")
    public ResultData configMode(String uid, int mode) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.WORK_MODE, mode, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config work mode: ").append(mode).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    //查询工作模式
    @GetMapping("/com/probe/mode")
    public ResultData probeMode(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.WORK_MODE, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query work mode) to the target uid: ").append(uid).toString());
        return result;
    }

    //风量
    @PostMapping("/com/config/speed")
    public ResultData configSpeed(String uid, int speed) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.FAN_SPEED, speed, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config fan speed: ").append(speed).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/speed")
    public ResultData probeSpeed(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.FAN_SPEED, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query fan speed) to the target uid: ").append(uid).toString());
        return result;
    }

    //废弃
    @PostMapping("/com/config/sterilise")
    public ResultData configSterilise(String uid, int mode) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.STERILISE_MODE, mode, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config sterilise mode: ").append(mode).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/sterilise")
    public ResultData probeSterilise(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.STERILISE_MODE, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query sterilise mode) to the target uid: ").append(uid).toString());
        return result;
    }

    @PostMapping("/com/config/heat")
    public ResultData configHeat(String uid, int heat) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.HEAT_MODE, heat, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config sterilise mode: ").append(heat).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/heat")
    public ResultData probeHeat(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.HEAT_MODE, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query heat mode) to the target uid: ").append(uid).toString());
        return result;
    }

    //废弃
    @PostMapping("c")
    public ResultData configCirculation(String uid, int circulation) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.CIRCULATION, circulation, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config sterilise mode: ").append(circulation).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/circulation")
    public ResultData probeCirculation(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.CIRCULATION, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query circulation mode) to the target uid: ").append(uid).toString());
        return result;
    }

    //屏显
    @PostMapping("/com/config/light")
    public ResultData configLight(String uid, int light) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.LIGHT, light, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config light mode: ").append(light).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/light")
    public ResultData probeLight(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.LIGHT, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query light mode) to the target uid: ").append(uid).toString());
        return result;
    }

    //重启
    @PostMapping("/com/reboot")
    public ResultData reboot(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        HeartBeatPacket packet = PacketUtil.generateReboot(uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a reboot packet to the target uid: ").append(uid).toString());
        return result;
    }

    @PostMapping("/com/config/lock")
    public ResultData configLock(String uid, int lock) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.CONFIG, PacketConstant.CHILD_LOCK, lock, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(config child lock: ").append(lock).append(") to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/lock")
    public ResultData probeLock(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        AbstractPacketV2 packet = PacketUtil.generateDetailProbe(Action.PROBE, PacketConstant.CHILD_LOCK, null, uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a probe packet(query child lock) to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/classification")
    public ResultData probeClassification(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        HeartBeatPacket packet = PacketUtil.generateClassification(uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a classification query packet to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/firmware")
    public ResultData probeFirmware(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        HeartBeatPacket packet = PacketUtil.generateFirmware(uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a firmware query packet to the target uid: ").append(uid).toString());
        return result;
    }

    @GetMapping("/com/probe/hardware")
    public ResultData probeHardware(String uid) {
        ResultData result = new ResultData();
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
        HeartBeatPacket packet = PacketUtil.generateHardware(uid);
        ctx.writeAndFlush(packet.convert2bytearray());
        result.setDescription(new StringBuffer("Succeed to send a hardware query packet to the target uid: ").append(uid).toString());
        return result;
    }
}
