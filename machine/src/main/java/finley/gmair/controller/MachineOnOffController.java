package finley.gmair.controller;

import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.model.machine.Machine_on_off;
import finley.gmair.service.MachineOnOffService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.impl.ActionNotifier;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/machine/power/onoff")
public class MachineOnOffController {

    @Autowired
    private MachineOnOffService machineOnOffService;

    @Autowired
    private ActionNotifier notifier;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    /**
     * The method is called to create new config
     * 1. verify the required parameters(uid, startTime, endTime)
     * 2. verify the uid, if exist, don't create
     * 3. create entity
     * 4. verify start or end is belong to now()-----now()+30
     * 5. if, new thread(()->{add to queue})
     * @return
     * */
    @PostMapping("/create")
    public ResultData createMachineOnOff(String qrcode, String startTime, String endTime) throws Exception {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("qrcode is wrong, please inspect");
            return result;
        }
        MachineQrcodeBindVo vo = ((List<MachineQrcodeBindVo>) response.getData()).get(0);

        condition.remove("codeValue");
        condition.put("machineId", vo.getMachineId());
        response = machineOnOffService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The config of the machine is already exists");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System error, please try again later");
            return result;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Timestamp start = new Timestamp(sdf.parse(startTime).getTime());
        Timestamp end = new Timestamp(sdf.parse(endTime).getTime());
        Machine_on_off machine_on_off = new Machine_on_off(vo.getMachineId(), start, end);
        response = machineOnOffService.create(machine_on_off);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Config create unsuccessfully");
            return result;
        }

        //获取当前时间点和30minute后时间点，验证新任务是否要直接加入队列
        new Thread(() -> {
            process(vo.getMachineId(), start.getTime(), end.getTime());
        }).start();

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * The method is called every half an hour
     * 1. condition(status: true, blockFlag, false)
     * 2. get list
     * 3. for(object : list)
     *  if start or end belong to now()-----now()+30
     *  then add to queue
     * @return
     * */
    @GetMapping("/schedule/half/list")
    public ResultData queryConfig() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("status", true);
        ResultData response = machineOnOffService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No config found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve config");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Machine_on_off> list = (List<Machine_on_off>) response.getData();

            //遍历list，获取30minute中要执行的定时任务，加入队列
            for (Machine_on_off machine_on_off : list) {
                String uid = machine_on_off.getMachineId();
                long start = machine_on_off.getStartTime().getTime();
                long end = machine_on_off.getEndTime().getTime();
                process(uid, start, end);
            }
            result.setData(list);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        return result;
    }

    /**
     * The method is called to update config
     * 1. verify uid is correct or not
     * 2. if correct, get config entity
     * 3. if new parameters(status, startTime, endTime) exist, set new parameters
     * 4. update config
     * 5. verify new parameters(startTime, endTime) is belong to now()---now()+30
     * @return
     * */
    @PostMapping("/update")
    public ResultData updateConfig(String qrcode, boolean status, String startTime, String endTime) throws Exception{
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("qrcode is wrong, please inspect");
            return result;
        }
        MachineQrcodeBindVo vo = ((List<MachineQrcodeBindVo>) response.getData()).get(0);

        condition.remove("codeValue");
        condition.put("machineId", vo.getMachineId());
        response = machineOnOffService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Parse machine config incorrect");
            return result;
        }

        Machine_on_off machine_on_off = ((List<Machine_on_off>) response.getData()).get(0);
        if (!StringUtils.isEmpty(status)) {
            machine_on_off.setStatus(status);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if (!StringUtils.isEmpty(startTime)) {
            machine_on_off.setStartTime(new Timestamp(sdf.parse(startTime).getTime()));
        }
        if (!StringUtils.isEmpty(endTime)) {
            machine_on_off.setEndTime(new Timestamp(sdf.parse(endTime).getTime()));
        }

        response = machineOnOffService.update(machine_on_off);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Update config unsuccessfully");
            return result;
        }

        machine_on_off = (Machine_on_off) response.getData();
        Timestamp start = machine_on_off.getStartTime();
        Timestamp end = machine_on_off.getEndTime();
        new Thread(() -> {
            process(vo.getMachineId(), start.getTime(), end.getTime());
        }).start();

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setDescription("Update config successfully");
        return result;
    }

    /**
     * The private method is called to add new uid to queue
     * parameter: 1. uid 2. start 3. end
     * */
    private void process(String uid, long start, long end) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            long now = sdf.parse(new Date().toString()).getTime();
            long after = now + 30 * 60 * 1000;
            if (start > now && start < after) {
                System.out.print(new StringBuilder("The machine: ").append(uid).append("is turned on"));
                notifier.sendTurnOn(uid);
            }
            if (end > now && end < after) {
                System.out.print(new StringBuilder("The machine: ").append(uid).append("is turned off"));
                notifier.sendTurnOff(uid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
