package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.form.installation.TeamForm;
import finley.gmair.model.installation.AssignReport;
import finley.gmair.service.InstallService;
import finley.gmair.service.ResourceService;
import finley.gmair.util.ExcelUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    @PostMapping("/assign/submit")
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description,String date) {
        ResultData result = new ResultData();
        if (org.apache.commons.lang.StringUtils.isEmpty(assignId) || org.apache.commons.lang.StringUtils.isEmpty(qrcode) || org.apache.commons.lang.StringUtils.isEmpty(picture) || wifi == null || org.apache.commons.lang.StringUtils.isEmpty(method)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装快照相关的信息");
            return result;
        }
        //存储二维码
        result=installService.initAssign(assignId,qrcode);
        if(result.getResponseCode()==ResponseCode.RESPONSE_ERROR){
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

    /**
     * 查询安装报告
     * @param assignId
     * @param teamId
     * @param memberId
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/assign/report")
    public ResultData report_query(String assignId,String teamId,String memberId,String beginTime,String endTime){
        ResultData result=new ResultData();
        if(!StringUtils.isEmpty(assignId)){
            result=installService.reportQueryByAssignId(assignId,beginTime,endTime);
        }else if(!StringUtils.isEmpty(memberId)){
            result=installService.reportQueryByMemberId(memberId,beginTime,endTime);
        }else if(!StringUtils.isEmpty(teamId)){
            result=installService.reportQueryByTeamId(teamId,beginTime,endTime);
        }else{
            result=installService.reportQueryByMemberTime(beginTime,endTime);
        }
        return result;
    }

    @GetMapping("/assign/report/download")
    public String report_download(String assignId,String teamId,String memberId,String beginTime,String endTime,HttpServletResponse response){
        ResultData result=new ResultData();
        if(!StringUtils.isEmpty(assignId)){
            result=installService.reportQueryByAssignId(assignId,beginTime,endTime);
        }else if(!StringUtils.isEmpty(memberId)){
            result=installService.reportQueryByMemberId(memberId,beginTime,endTime);
        }else if(!StringUtils.isEmpty(teamId)){
            result=installService.reportQueryByTeamId(teamId,beginTime,endTime);
        }else{
            result=installService.reportQueryByMemberTime(beginTime,endTime);
        }
        String fileName=new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".xls";
        try {
            //查询数据库中所有的数据
            String data=JSONObject.toJSONString(result.getData());
            List<AssignReport> list=JSONArray.parseArray(data,AssignReport.class);
//            logger.info(result.getData().toString());
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("sheet1");
            String[] n = { "编号", "二维码", "型号", "用户" ,"联系方式","联系地址","来源","团队","安装工人","完成时间"};
            Object[][] value = new Object[list.size() + 1][n.length];
            for (int m = 0; m < n.length; m++) {
                value[0][m] = n[m];
            }
            for (int i = 0; i < list.size(); i++) {
                value[i + 1][0] = list.get(i).getAssignId();
                value[i + 1][1] = list.get(i).getCodeValue();
                value[i + 1][2] = list.get(i).getAssignDetail();
                value[i + 1][3] = list.get(i).getConsumerConsignee();
                value[i + 1][4] = list.get(i).getConsumerPhone();
                value[i + 1][5] = list.get(i).getConsumerAddress();
                if(StringUtils.isEmpty(list.get(i).getAssignSource())){
                    value[i + 1][6] = "无";
                }else {
                    value[i + 1][6] = list.get(i).getAssignSource();
                }
                value[i + 1][7] = list.get(i).getTeamName();
                value[i + 1][8] = list.get(i).getMemberName();
                value[i + 1][9] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreateAt());
            }
            HSSFRow row[]=new HSSFRow[list.size()+1];
            HSSFCell cell[]=new HSSFCell[n.length];
            for(int i=0;i<row.length;i++){
                row[i]=sheet.createRow(i);
                for(int j=0;j<cell.length;j++){
                    cell[j]=row[i].createCell(j);
                    cell[j].setCellValue(value[i][j].toString());
                }
            }
            OutputStream os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            wb.write(os);
            os.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(path);
        if (!file.exists()) {
            logger.error("未能找到文件: " + path);
        }
        try {
            Workbook book = WorkbookFactory.create(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            book.write(byteArrayOutputStream);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            response.setContentLength(byteArrayOutputStream.size());
            ServletOutputStream outputstream = response.getOutputStream();
            byteArrayOutputStream.writeTo(outputstream);
            byteArrayOutputStream.close();
            outputstream.flush();
            outputstream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }
}