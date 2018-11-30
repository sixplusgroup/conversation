package finley.gmair.controller;

import finley.gmair.model.timing.Task;
import finley.gmair.service.TaskService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/list")
    public ResultData getTask() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = taskService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No task found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve task from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/create")
    public ResultData createTask(String taskName, String frequent, String description) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(taskName) || StringUtils.isEmpty(frequent) || StringUtils.isEmpty(description)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Task task = new Task(taskName, frequent, description);
        ResultData response = taskService.create(task);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Insert task unsuccessfully");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @PostMapping(value = "/update")
    public ResultData updateTask(String taskName, boolean status, String frequent, String description) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(taskName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("taskName", taskName);
        condition.put("blockFlag", false);
        ResultData response = taskService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Can't get task with the task name: ").append(taskName).toString());
            return result;
        }
        Task task = ((List<Task>) response.getData()).get(0);
        if (!StringUtils.isEmpty(status)) {
            task.setStatus(status);
        }
        if (!StringUtils.isEmpty(frequent)) {
            task.setFrequent(frequent);
        }
        if (!StringUtils.isEmpty(description)) {
            task.setDescription(description);
        }
        response = taskService.modify(task);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Update task unsuccessfully");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
