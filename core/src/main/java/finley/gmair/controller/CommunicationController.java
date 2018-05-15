package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.netty.GMRepository;
import finley.gmair.service.HeartbeatPacketService;
import finley.gmair.util.CoreProperties;
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

import javax.xml.transform.Result;


@RestController
@RequestMapping("/core")
public class CommunicationController {

    @Autowired
    private GMRepository repository;

    @Autowired
    private HeartbeatPacketService heartbeatPacketService;

    /**
     * This will issue a heartbeat packet to the target uid
     * if the uid does not exist in the cache, it will not send the packet
     *
     * @param uid
     * @return
     */
    @GetMapping("/com/heartbeat")
    public ResultData heartbeat(String uid) {
        ResultData result = new ResultData();
        HeartBeatPacket packet = PacketUtil.generateHeartBeat(uid);
        ChannelHandlerContext ctx = repository.retrieve(uid);
        if (StringUtils.isEmpty(ctx)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("No channel found for uid: ").append(uid).toString());
            return result;
        }
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

        return result;
    }

    @PostMapping("/com/config/address")
    public ResultData configAddress(String uid, String address) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/address")
    public ResultData probeAddress(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/port")
    public ResultData configPort(String uid, int port) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/port")
    public ResultData probePort(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/heartbeat")
    public ResultData configHeartbeatCycle(String uid, int interval) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/heartbeat")
    public ResultData probeHeartbeatCycle(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/power")
    public ResultData configPower(String uid, int power) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/power")
    public ResultData probePower(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/mode")
    public ResultData configMode(String uid, int mode) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/mode")
    public ResultData probeMode(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/speed")
    public ResultData configSpeed(String uid, int speed) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/speed")
    public ResultData probeSpeed(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/sterilise")
    public ResultData configSterilise(String uid, int mode) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/sterilise")
    public ResultData probeSterilise(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/sterilises")
    public ResultData configHeat(String uid, int heat) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/heat")
    public ResultData probeHeat(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/circulation")
    public ResultData configCirculation(String uid, int circulation) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/circulation")
    public ResultData probeCirculation(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/light")
    public ResultData configLight(String uid, int light) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/light")
    public ResultData probeLight(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/reboot")
    public ResultData reboot(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/com/config/lock")
    public ResultData configLock(String uid, int lock) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/lock")
    public ResultData probeLock(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/classification")
    public ResultData probeClassification() {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/firmware")
    public ResultData probeFirmware(String uid) {
        ResultData result = new ResultData();

        return result;
    }

    @GetMapping("/com/probe/hardware")
    public ResultData probeHardware(String uid) {
        ResultData result = new ResultData();

        return result;
    }
}
