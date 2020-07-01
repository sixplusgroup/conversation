package finley.gmair.controller;

import finley.gmair.model.machine.Machine_on_off;
import finley.gmair.service.MachineOnOffService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.PreBindService;
import finley.gmair.service.impl.ActionNotifier;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/power/onoff")
public class MachineOnOffController {
    private Logger logger = LoggerFactory.getLogger(MachineOnOffController.class);

    @Autowired
    private MachineOnOffService machineOnOffService;

    @Autowired
    private ActionNotifier notifier;

    @Autowired
    private PreBindService preBindService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    /**
     * The method is called to create new config
     * 1. verify the required parameters(uid, startTime, endTime)
     * 2. verify the uid, if exist, don't create
     * 3. create entity
     * 4. verify start or end is belong to now()-----now()+30
     * 5. if, new thread(()->{add to queue})
     *
     * @return
     */
    @PostMapping("/confirm")
    public ResultData configConfirm(String qrcode, int startHour, int startMinute, int endHour, int endMinute, boolean status) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(startHour) || StringUtils.isEmpty(startMinute)
                || StringUtils.isEmpty(endHour) || StringUtils.isEmpty(endMinute) || StringUtils.isEmpty(status)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machine_id是否获取成功
        response = preBindService.checkMachineId(response, result, qrcode);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            return response;
        }

        MachineQrcodeBindVo vo = ((List<MachineQrcodeBindVo>) response.getData()).get(0);

        LocalTime start = LocalTime.of(startHour, startMinute, 0);
        LocalTime end = LocalTime.of(endHour, endMinute, 0);
        Machine_on_off machine_on_off = new Machine_on_off(vo.getMachineId(), start, end);
        machine_on_off.setStatus(status);

        condition.remove("codeValue");
        condition.put("machineId", vo.getMachineId());
        response = machineOnOffService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System error, please try again later");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            String configId = ((List<Machine_on_off>) response.getData()).get(0).getConfigId();
            machine_on_off.setConfigId(configId);
            response = machineOnOffService.update(machine_on_off);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            response = machineOnOffService.create(machine_on_off);
            result.setData(response.getData());
        }

        //获取当前时间点和30minute后时间点，验证新任务是否要直接加入队列
        new Thread(() -> {
            process(vo.getMachineId(), start, end);
        }).start();

        return result;
    }

    /**
     * The method is called every half an hour
     * 1. condition(status: true, blockFlag, false)
     * 2. get list
     * 3. for(object : list)
     * if start or end belong to now()-----now()+30
     * then add to queue
     *
     * @return
     */
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
                LocalTime start = machine_on_off.getStartTime();
                LocalTime end = machine_on_off.getEndTime();
                process(uid, start, end);
            }
            result.setData(list);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        return result;
    }

    /**
     * The method is called to get record by qrcode
     * 1. verify the code
     * 2. get machineId by code
     * 3. get record by machineId
     *
     * @return
     */
    @GetMapping(value = "/get/record/by/code")
    public ResultData getRecord(String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);

        // 检查machine_id是否获取成功
        response = preBindService.checkMachineId(response, result, qrcode);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            return response;
        }

        MachineQrcodeBindVo vo = ((List<MachineQrcodeBindVo>) response.getData()).get(0);

        condition.remove("codeValue");
        condition.put("machineId", vo.getMachineId());
        response = machineOnOffService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Parse machine incorrect");
            return result;
        }

        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * The private method is called to add new uid to queue
     * parameter: 1. uid 2. start 3. end
     */
    private void process(String uid, LocalTime start, LocalTime end) {
        Calendar calendar = Calendar.getInstance();
        //计算5分钟前的时间
        calendar.add(Calendar.MINUTE, -5);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        LocalTime before = LocalTime.of(hour, minute, second);
        calendar.add(Calendar.MINUTE, 10);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        LocalTime after = LocalTime.of(hour, minute, second);
        try {
            if (start.isAfter(before) && start.isBefore(after)) {
                notifier.sendTurnOn(uid);
            }
            if (end.isAfter(before) && end.isBefore(after)) {
                notifier.sendTurnOff(uid);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
