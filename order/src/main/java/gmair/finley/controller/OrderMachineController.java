package gmair.finley.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import finley.gmair.model.machine.Insight;
import finley.gmair.model.order.MachineItem;
import finley.gmair.model.order.MachineMission;
import finley.gmair.model.order.MachineMissionStatus;
import finley.gmair.pagination.DataTablePage;
import finley.gmair.pagination.DataTableParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.MachineItemVo;
import gmair.finley.form.MachineForm;
import gmair.finley.form.MachineMissionForm;
import gmair.finley.service.MachineItemService;
import gmair.finley.service.MachineMissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderMachineController {

    private Logger logger = LoggerFactory.getLogger(OrderMachineController.class);

    @Autowired
    private MachineMissionService machineMissionService;

    @Autowired
    private MachineItemService machineItemService;


    @RequestMapping(method = RequestMethod.GET, value = "/machineMission/list/{machineItemId}")
    public ResultData machineMissionlist(@PathVariable String machineItemId) {
        ResultData result = new ResultData();

        Map<String, Object> missionCondition = new HashMap<>();
        missionCondition.put("blockFlag", false);
        missionCondition.put("machineItemId", machineItemId);
        ResultData response = machineMissionService.fetch(missionCondition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/machinemission/create")
    public ResultData createMachineMission(@Valid MachineMissionForm form, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("表单中含有非法数据");
            logger.error(JSON.toJSONString(br.getAllErrors()));
            return result;
        }

//        Subject subject = SecurityUtils.getSubject();
//        UserVo userVo = (UserVo) subject.getPrincipal();
        MachineMission machineMission =
                new MachineMission(form.getMachineId(), form.getMissionTitle(), form.getMissionContent(),
                        "123456", Timestamp.valueOf(form.getMissionDate()));
        ResultData response = machineMissionService.create(machineMission);
        result.setResponseCode(response.getResponseCode());
        // update machine status
        if (form.getMachineStatusCode() != null) {
            MachineItem machineItem = new MachineItem();
            machineItem.setMachineId(form.getMachineId());
            machineItem.setInstallType(form.getMachineInstallType());
            machineItem.setMachineCode(form.getMachineQrcode());
            machineItem.setProviderId(form.getMachineProvider());
            machineItem.setMachineMissionStatus(
                    MachineMissionStatus.convertToMissionStatus(Integer.parseInt(form.getMachineStatusCode())));
            machineItemService.update(machineItem);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK && form.getFilePathList() != null) {
            result.setData(response.getData());
            MachineMission mission = (MachineMission) response.getData();
            String machine = mission.getMachineItemId();
            String missionId = mission.getMissionId();
            JSONArray filepathList = JSON.parseArray(form.getFilePathList());
            for (Object filepath : filepathList) {
                Insight insight = new Insight();
                insight.setMachineId(machine);
                insight.setEventId(missionId);
                insight.setPath((String) filepath);
//                qRCodeService.createInsight(insight);
            }
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/machine/item/{machineItem}")
    public ModelAndView machineItemView(@PathVariable String machineItem) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineItem);
        condition.put("blockFlag", false);
        ResultData response = machineItemService.fetch(condition);
        MachineItemVo machineItemVo = ((List<MachineItemVo>) response.getData()).get(0);
        view.addObject("machineItem", machineItemVo);
        view.setViewName("/backend/order/orderMachine");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orderMachine/detail")
    public ResultData getMachineItem(@RequestParam String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = machineItemService.fetch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/machineOverview/list")
    public ModelAndView orderMachineView() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/order_machine_overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/machineItem/list")
    public DataTablePage<MachineItemVo> machineItemList(DataTableParam param) {
        ResultData response = machineItemService.fetch(param);
        DataTablePage<MachineItemVo> records = new DataTablePage<>();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return records;
        } else {
            return (DataTablePage<MachineItemVo>) response.getData();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/machineItem/list")
    public ResultData getMachineItemList(@RequestParam(required = false) String param) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        ResultData response = machineItemService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/machineItem/update")
    public ResultData updateMachine(MachineForm machineForm) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineForm.getMachineId());
        condition.put("blockFlag", 0);
        ResultData response = machineItemService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            MachineItemVo machineItemVo = ((List<MachineItemVo>) response.getData()).get(0);
            MachineItem machineItem = new MachineItem();
            machineItem.setMachineId(machineItemVo.getMachineId());
            machineItem.setProviderId(machineForm.getMachineProvider());
            machineItem.setInstallType(machineForm.getMachineInstallType());
            machineItem.setMachineCode(machineForm.getMachineCode());
            machineItem.setMachineMissionStatus(machineItemVo.getMachineMissionStatus());
            machineItem.setOrderItemId(machineItemVo.getOrderItemId());
            response = machineItemService.update(machineItem);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
            } else {
                result.setData(response.getData());
            }
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
    }
}
