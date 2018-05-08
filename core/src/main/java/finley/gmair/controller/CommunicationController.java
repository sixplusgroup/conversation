package finley.gmair.controller;

import finley.gmair.model.core.HeartbeatPacket;
import finley.gmair.service.HeartbeatPacketService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/core")
public class CommunicationController {

//    @Autowired
//    @Qualifier("repository")
//    private GMRepository repository;

    @Autowired
    private HeartbeatPacketService heartbeatPacketService;

    @GetMapping("/one")
    public String test() {
        return null;
    }

    /**
     * This method is used to create heartbeat packet in the system
     *
     * @return
     */
    @PostMapping("/heartbeatPacket/create")
    public ResultData addHeartbeatPacket(HeartbeatPacket form){
        ResultData result = new ResultData();
        String FRH = form.getFRH();
        String CTF = form.getCTF();
        String CID = form.getCID();
        String UID = form.getUID();
        long TIME = form.getTIME();
        String LEN = form.getLEN();
        String DATA = form.getDATA();
        String CRC = form.getCRC();
        String FRT = form.getFRT();
        HeartbeatPacket heartbeatPacket = new HeartbeatPacket(FRH, CTF, CID, UID, TIME, LEN, DATA, CRC, FRT);
        ResultData response = heartbeatPacketService.createHeartbeatPacket(heartbeatPacket);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to add heartbeat packet with UID: ").append(UID).toString());
        }
        return result;
    }

    /**
     * This method is used to query heartbeat packet by UID
     *
     * @return
     */
    @GetMapping("/heartbeatPacket/query/{UID}")
    public ResultData queryHeartbeatPacket(@PathVariable String UID){
        ResultData result = new ResultData();
        ResultData response = heartbeatPacketService.fetchHeartbeatPacket(UID);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }else{
            return response;
        }
    }

}
