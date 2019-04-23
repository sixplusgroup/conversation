package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.form.installation.TeamForm;
import finley.gmair.service.InstallService;
import finley.gmair.util.ExcelUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;

@CrossOrigin
@RestController
@RequestMapping("/management/install")
@PropertySource("classpath:management.properties")
public class InstallController {
    private Logger logger = LoggerFactory.getLogger(InstallController.class);

    @Autowired
    private InstallService installService;

    @Value("${temp_path}")
    private String baseDir;

    @RequestMapping(value = "/assign/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        //存储文件
        MultipartFile file = request.getFile("file");
        String name = file.getOriginalFilename();
        File base = null;
        try {
            base = new File(baseDir);
            if (!base.exists()) base.mkdirs();
        } catch (Exception e) {
            if (base != null) base.mkdirs();
        }
        File target = new File(baseDir + File.separator + name);
        try {
            file.transferTo(target);
        } catch (Exception e) {
            logger.error("文件: " + name + "处理失败, " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("文件上传失败");
            return result;
        }
        //解析文件
        try {
            Workbook book = WorkbookFactory.create(target);
            int nums = book.getNumberOfSheets();
            if (nums <= 0) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("请确保上传的文件中有列表数据");
                return result;
            }
            //获取第一页的数据
            Sheet sheet = book.getSheetAt(0);
            JSONArray data = ExcelUtil.decode(sheet);
            result.setData(data);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("文件未能成功解析");
        }
        return result;
    }

    @PostMapping("/assign/create")
    public ResultData create(AssignForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getConsumerConsignee()) || StringUtils.isEmpty(form.getConsumerPhone())
                || StringUtils.isEmpty(form.getConsumerAddress())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入所有的安装任务用户相关的信息");
            return result;
        }
        String consumerConsignee = form.getConsumerConsignee();
        String consumerPhone = form.getConsumerPhone();
        String consumerAddress = form.getConsumerAddress();
        String model = form.getModel();
        if (StringUtils.isEmpty(form.getDescription())) {
            result = installService.createAssign(consumerConsignee, consumerPhone, consumerAddress, model);
        } else {

        }
        return result;
    }

    @PostMapping("/assign/cancel")
    public ResultData cancel(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请选择相应的安装任务");
            return result;
        }
        result = installService.cancelAssign(assignId);
        return result;
    }

    @GetMapping("/assign/list")
    public ResultData assigns(String status, String teamId, Integer start, Integer length) {
        ResultData result;
        if (start == null || length == null) {
            result = installService.fetchAssign(status, teamId);
        } else {
            result = installService.fetchAssignByPage(status, teamId, start, length);
        }
        return result;
    }

    @GetMapping("/assign/{assignId}/info")
    public ResultData assign(@PathVariable("assignId") String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入安装任务的编号");
            return result;
        }
        result = installService.fetchAssign(assignId);
        return result;
    }

    @PostMapping("/assign/dispatch")
    public ResultData dispatch(String assignId, String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入安装任务和派单团队信息");
            return result;
        }
        result = installService.dispatchAssign(assignId, teamId);
        return result;
    }

    @PostMapping("/team/create")
    public ResultData create(TeamForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getTeamName()) || StringUtils.isEmpty(form.getTeamArea())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装团队的信息");
            return result;
        }
        result = installService.createTeam(form.getTeamName(), form.getTeamArea(), form.getTeamDescription());
        return result;
    }

    @GetMapping("/team/list")
    public ResultData teams(Integer start, Integer length) {
        ResultData result;
        if (start == null || length == null) {
            result = installService.fetchTeam();
        } else {
            result = installService.fetchTeam(start, length);
        }
        return result;
    }

    @GetMapping("/team/{teamId}/info")
    public ResultData team(@PathVariable("teamId") String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保团队ID信息已提供");
            return result;
        }
        result = installService.fetchTeam(teamId);
        return result;
    }

    @PostMapping("/leader/watch")
    public ResultData watch(String memberId, String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供用户和团队信息");
            return result;
        }
        result = installService.watch(memberId, teamId);
        return result;
    }
}