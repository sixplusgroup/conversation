package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.form.installation.TeamForm;
import finley.gmair.service.InstallService;
import finley.gmair.service.ResourceService;
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

    @Autowired
    private ResourceService resourceService;

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
                || StringUtils.isEmpty(form.getConsumerAddress()) || StringUtils.isEmpty(form.getSource())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入所有的安装任务用户相关的信息");
            return result;
        }
        String consumerConsignee = form.getConsumerConsignee();
        String consumerPhone = form.getConsumerPhone();
        String consumerAddress = form.getConsumerAddress();
        String model = form.getModel();
        String source = form.getSource();
        if (StringUtils.isEmpty(form.getDescription())) {
            result = installService.createAssign(consumerConsignee, consumerPhone, consumerAddress, model, source);
        } else {
            result = installService.createAssign(consumerConsignee, consumerPhone, consumerAddress, model, source, form.getDescription());
        }
        return result;
    }

    @PostMapping("/assign/cancel")
    public ResultData cancel(String assignId, String message) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(message)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请选择相应的安装任务");
            return result;
        }
        result = installService.cancelAssign(assignId, message);
        return result;
    }

    @GetMapping("/assign/list")
    public ResultData assigns(String status, String teamId, Integer start, Integer length,String search) {
        ResultData result;
        if (start == null || length == null) {
            result = installService.fetchAssign(status, teamId , search);
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

    /**
     * 调度人员补录安装快照
     *
     * @param assignId
     * @param qrcode
     * @param picture
     * @param wifi
     * @param method
     * @param description
     * @return
     */
    @PostMapping("/assign/record")
    public ResultData record(String assignId, String qrcode, String picture, Boolean wifi, String method, String description) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture) || StringUtils.isEmpty(wifi) || StringUtils.isEmpty(method) || StringUtils.isEmpty(description)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供完整的安装快照的信息");
            return result;
        }
        //提交安装图片源

        //补录安装任务信息

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

    /**
     * 获取安装任务的流水信息
     *
     * @param assignId 安装任务ID
     * @return
     */
    @GetMapping("/trace")
    public ResultData trace(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保任务ID信息已提供");
            return result;
        }
        result = installService.trace(assignId);
        return result;
    }

    /**
     * 根据teamId获取团队成员信息
     *
     * @param teamId
     * @return
     */
    @GetMapping("/team/member")
    public ResultData obtainTeamMember(String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保团队ID信息已提供");
            return result;
        }
        result = installService.fetchTeamMember(teamId);
        return result;
    }
    /**
     * 创建团队成员
     *
     * @param teamId
     * @param memberPhone
     * @param memberName
     * @param memberRole
     * @return
     */

    @PostMapping("/member/create")
    public ResultData createTeamMember(String teamId,String memberPhone,String memberName,int memberRole) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保团队ID信息已提供");
            return result;
        }
        result = installService.createTeamMember(teamId,memberPhone,memberName,memberRole);
        return result;
    }

    /**
     * 修改联系方式
     * @param memberPhone
     * @param memberId
     * @return
     */

    @PostMapping("/member/update")
    public ResultData updatePhone(String memberPhone,String memberId){
        ResultData result=new ResultData();
        if(StringUtils.isEmpty(memberId)||StringUtils.isEmpty(memberPhone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保输入了正确的memberId和需要更新的联系方式");
            return result;
        }
        result=installService.updatePhone(memberPhone,memberId);
        return result;
    }

    /**
     * 根据memberId删除成员
     * @param memberId
     * @return
     */

    @GetMapping("/member/block")
    public ResultData deleteMember(String memberId){
        ResultData result=new ResultData();
        if(StringUtils.isEmpty(memberId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入成员ID");
            return result;
        }
        result=installService.deleteMember(memberId);
        return result;
    }

    /**
     * 管理员获取任务快照
     * @param assignId
     * @return
     */
    @GetMapping("/assign/snapshot")
    public ResultData snapshot(String assignId){
        ResultData result=new ResultData();
        if(StringUtils.isEmpty(assignId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入任务ID");
            return result;
        }
        result=installService.snapshot(assignId);
        return result;
    }

    @GetMapping("/team/block")
    public ResultData deleteTeam(String teamId){
        ResultData result=new ResultData();
        if(StringUtils.isEmpty(teamId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供团队ID");
            return result;
        }
        result=installService.deleteTeam(teamId);
        return result;
    }

    /**
     * 管理员提交安装快照
     * @param assignId
     * @param qrcode
     * @param picture
     * @param wifi
     * @param method
     * @param description
     * @return
     */

    @PostMapping("assign/submit")
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description,String date) {
        ResultData result = new ResultData();
        if (org.apache.commons.lang.StringUtils.isEmpty(assignId) || org.apache.commons.lang.StringUtils.isEmpty(qrcode) || org.apache.commons.lang.StringUtils.isEmpty(picture) || wifi == null || org.apache.commons.lang.StringUtils.isEmpty(method)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装快照相关的信息");
            return result;
        }
        //提交安装图片资源
        result = resourceService.save(picture);
        if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return result;
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(description)) {
            result = installService.submitAssign(assignId, qrcode, picture, wifi, method,date);
        } else {
            result = installService.submitAssign(assignId, qrcode, picture, wifi, method, description,date);
        }
        return result;
    }
}