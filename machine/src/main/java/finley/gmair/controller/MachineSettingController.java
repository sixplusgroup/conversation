package finley.gmair.controller;

import finley.gmair.form.machine.LightSettingForm;
import finley.gmair.form.machine.MachineSettingForm;
import finley.gmair.form.machine.VolumeSettingForm;
import finley.gmair.model.machine.v2.*;
import finley.gmair.service.MachineSettingService;
import finley.gmair.service.ScreenRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/setting")
public class MachineSettingController {

    @Autowired
    private MachineSettingService machineSettingService;

    @Autowired
    private ScreenRecordService screenRecordService;

    @GetMapping("/powerAction/list")
    public ResultData powerActionList() {
        return machineSettingService.fetchPowerActionMachine();
    }

    /**
     * The method is provided the interface of creating machine setting
     *
     * @return
     * */
    @PostMapping("/create")
    public ResultData createMachineSetting(MachineSettingForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getConsumerId()) || StringUtils.isEmpty(form.getSettingName())
                || StringUtils.isEmpty(form.getCodeValue())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", form.getCodeValue());
        ResultData response = machineSettingService.fetchMachineSetting(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }

        MachineSetting setting = new MachineSetting(form.getConsumerId(), form.getSettingName(), form.getCodeValue());
        response = machineSettingService.createMachineSetting(setting);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store machine setting");
        return result;
    }

    /**
     * The method is called for getting the user's volume setting with codeValue
     *
     * @return
     */
    @GetMapping("/volumesetting/list/{codeValue}")
    public ResultData volumeSettingList(@PathVariable("codeValue") String codeValue) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        return machineSettingService.fetchVolumeSetting(condition);
    }

    /**
     * the method is used to insert or update volume setting
     * according to the volume whether exist or not
     * exist -> update. otherwise, insert
     *
     * @return
    */
    @PostMapping("/volume/action")
    public ResultData volumeAction(VolumeSettingForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getCodeValue()) || StringUtils.isEmpty(form.getFloorPm2_5())
                || StringUtils.isEmpty(form.getUpperPm2_5()) || StringUtils.isEmpty(form.getSpeedValue())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("codeValue", form.getCodeValue());
        ResultData response = machineSettingService.fetchMachineSetting(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        MachineSetting machineSettingVo = ((List<MachineSetting>) response.getData()).get(0);

        String settingId = machineSettingVo.getSettingId().trim();
        int floorPm2_5 = form.getFloorPm2_5();
        int upperPm2_5 = form.getUpperPm2_5();
        int speedValue = form.getSpeedValue();
        VolumeSetting volumeSetting = new VolumeSetting(settingId, floorPm2_5, upperPm2_5, speedValue);

        condition.clear();
        condition.put("settingId", settingId);
        response = machineSettingService.fetchVolumeSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = machineSettingService.modifyVolumeSetting(volumeSetting);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result = machineSettingService.createVolumeSetting(volumeSetting);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query errors, please try again later");
        }
        return result;
    }

    /**
     * The method is called for getting the user's light setting with codeValue
     *
     * @return
     */
    @GetMapping("/light/{codeValue}")
    public ResultData lightSettingList(@PathVariable("codeValue") String codeValue) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        return machineSettingService.fetchLightSetting(condition);
    }

    /**
     * the method is used to insert or update light setting
     * according to the light whether exist or not
     * exist -> update. otherwise, insert
     *
     * @return
     */
    @PostMapping("/light/action")
    public ResultData lightAction(LightSettingForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getCodeValue()) || StringUtils.isEmpty(form.getLightValue())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("codeValue", form.getCodeValue());
        ResultData response = machineSettingService.fetchMachineSetting(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }

        MachineSetting machineSettingVo = ((List<MachineSetting>) response.getData()).get(0);
        String settingId = machineSettingVo.getSettingId();
        int lightValue = form.getLightValue();
        LightSetting setting = new LightSetting(settingId, lightValue);

        condition.clear();
        condition.put("settingId", settingId);
        response = machineSettingService.fetchLightSetting(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = machineSettingService.modifyLightSetting(setting);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result = machineSettingService.createLightSetting(setting);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query errors, please try again later");
        }
        return result;
    }

    /**
     * The method is called for getting the global setting in machine
     *
     * @return
     */
    @GetMapping("/global/list")
    public ResultData globalMachineSettingList() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        return machineSettingService.fetchGlobalMachineSetting(condition);
    }

    /**
     * The method is called to set global machine setting
     * Insert the setting to database
     *
     * @return
     */
    @PostMapping("/global/create")
    public ResultData createGlobalMachineSetting(String settingName) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(settingName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please fill in the required fields");
            return result;
        }
        GlobalMachineSetting setting = new GlobalMachineSetting(settingName);
        ResultData response = machineSettingService.createGlobalMachineSetting(setting);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store global machine setting");
        return result;
    }

    /**
     * The method is called for getting the user's changing record of screen
     * According to user's machine codeValue
     *
     * @return
     */
    @GetMapping(value = "/screen/record/list")
    public ResultData screenRecordList(String codeValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
//        codeValue is not null, only query refer to codeValue
//        codeValue is null, query all
        if (!StringUtils.isEmpty(codeValue)) {
            condition.put("codeValue", codeValue);
        }
        condition.put("blockFlag", false);
        ResultData response = screenRecordService.fetchScreenRecord(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Record list is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * The method is called to add screen record when changing the screen
     * Insert the record to database
     *
     * @return
     */
    @PostMapping(value = "/screen/record/create")
    public ResultData createScreenRecord(String codeValue) throws Exception {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(codeValue)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please fill in the required fields");
            return result;
        }
        // get current date
        Date dateNow = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(dateNow);

        ScreenRecord record = new ScreenRecord(codeValue, date);
        ResultData response = screenRecordService.createScreenRecord(record);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store record to database");
        return result;
    }
}
