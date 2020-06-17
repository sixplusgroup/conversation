package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.machine.PreBindForm;
import finley.gmair.form.machine.QRCodeCreateForm;
import finley.gmair.form.machine.QRCodeForm;
import finley.gmair.model.goods.GoodsModel;
import finley.gmair.model.machine.*;
import finley.gmair.service.*;
import finley.gmair.util.*;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * qrcode is a representation of machine once it is bind with a machine id
 * user's settings will be related to qrcode
 * qrcode should have several status, created, assigned, occupied, recalled
 */
@RestController
@RequestMapping("/machine/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PreBindService preBindService;

    @Autowired
    private IdleMachineService idleMachineService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @Autowired
    private BoardVersionService boardVersionService;

    @Autowired
    private CoreV2Service coreV2Service;

    @Autowired
    private CoreV1Service coreV1Service;

    @Autowired
    private CoreV3Service coreV3Service;

    /**
     * This method is used to create a record of qrcode
     *
     * @return
     */
    @PostMapping("/create/one")
    public ResultData createOne(QRCodeForm form) {
        ResultData result = new ResultData();
        String modelId = form.getModelId();
        if (StringUtils.isEmpty(modelId) || StringUtils.isEmpty(form.getGoodsId()) || StringUtils.isEmpty(form.getBatchValue())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        ResultData response = goodsService.fetchModel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Incorrect modelId parameter: " + modelId);
            return result;
        }
        GoodsModel gmVo = ((List<GoodsModel>) response.getData()).get(0);
        String batch = new StringBuffer(gmVo.getModelCode()).append(form.getBatchValue()).toString();
        response = qrCodeService.create(form.getGoodsId(), form.getModelId(), batch, 1);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("The qrCode generated successfully");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Sorry, the qrCode is not generated as expected, please try again");
        return result;
    }

    /**
     * This method is used to create a batch of qrcode
     *
     * @return
     */
    @PostMapping("/create")
    public ResultData create(QRCodeCreateForm form) {
        ResultData result = new ResultData();
        String modelId = form.getModelId();
        if (StringUtils.isEmpty(modelId) || StringUtils.isEmpty(form.getBatchValue()) || StringUtils.isEmpty(form.getGoodsId()) || StringUtils.isEmpty(form.getNum())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        ResultData response = goodsService.fetchModel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Incorrect modelId parameter: " + modelId);
            return result;
        }
        GoodsModel gmVo = ((List<GoodsModel>) response.getData()).get(0);
        String batch = new StringBuffer(gmVo.getModelCode()).append(form.getBatchValue()).toString();
        response = qrCodeService.create(form.getGoodsId(), form.getModelId(), batch, form.getNum());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            new Thread(() -> {
                String filename = generateXls(batch);
            }).start();
            result.setDescription("The qrCodes are generated successfully");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Sorry, the qrCodes are not generated as expected, please try again");
        return result;
    }

    /**
     * This method is used to generate the qrcode xls when create batch qrcode
     * method is private, just called in create batch
     *
     * @return
     */
    private String generateXls(String batch) {
        if (StringUtils.isEmpty(batch)) {
            return new StringBuffer("The request with no specified batchNo cannot be executed").toString();
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("batchValue", batch);
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return new StringBuffer("The request with batchNo: ").append(batch).append(" cannot be executed").toString();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("qrCode");

        //set table headers
        Row row0 = sheet.createRow(0);
        Cell headerCell0 = row0.createCell(0);
        headerCell0.setCellValue("序号");
        Cell headerCell1 = row0.createCell(1);
        headerCell1.setCellValue("型号");
        Cell headerCell2 = row0.createCell(2);
        headerCell2.setCellValue("批号");
        Cell headerCell3 = row0.createCell(3);
        headerCell3.setCellValue("二维码");
        Cell headerCell4 = row0.createCell(4);
        headerCell4.setCellValue("URL");

        //insert data to table
        List<QRCode> qrCodes = (List<QRCode>) response.getData();
        for (int row = 1, i = 0; i < qrCodes.size(); i++, row++) {
            QRCode qrCode = qrCodes.get(i);
            Row current = sheet.createRow(row);
            Cell cell1 = current.createCell(0);
            cell1.setCellValue(i + 1);
            Cell modelId = current.createCell(1);
            modelId.setCellValue(qrCode.getModelId().trim());
            Cell batchValue = current.createCell(2);
            batchValue.setCellValue(qrCode.getBatchValue().trim());
            Cell codeValue = current.createCell(3);
            codeValue.setCellValue(qrCode.getCodeValue().trim());
            Cell codeUrl = current.createCell(4);
            codeUrl.setCellValue(qrCode.getCodeUrl().trim());
        }

        //open file and write data
        String base = MachineProperties.getValue("qrcode_storage_path");
        File directory = new File(new StringBuffer(base).append(MachineProperties.getValue("qrcode_batch")).toString());
        if (!directory.exists()) {
            directory.mkdir();
        }
        String tempSerial = IDGenerator.generate("QRC");
        File file = new File(
                new StringBuffer(base).append(MachineProperties.getValue("qrcode_batch")).append(tempSerial).append(".xlsx").toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempSerial;
    }

    /**
     * This method is used to download a batch of qrcode
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/download/{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        File file = null;
        if (filename.startsWith("QRC")) {
            filename = filename + ".xlsx";
            file = new File(MachineProperties.getValue("qrcode_storage_path") + MachineProperties.getValue("qrcode_batch") + filename);
        }
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = inputStream.read(buffer, 0, buffer.length))) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to create finish qrcode prebind
     * update idle after prebind
     *
     * @return
     */
    @PostMapping(value = "/prebind")
    public ResultData preBind(PreBindForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getMachineId()) || StringUtils.isEmpty(form.getCodeValue())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        PreBindCode code = new PreBindCode(form.getMachineId(), form.getCodeValue());
        ResultData response = preBindService.create(code);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            //insert correct, then update idleMachine
//            Map<String, Object> condition = new HashMap<>();
//            condition.put("machineId", form.getMachineId());
//            response = idleMachineService.modify(condition);
//            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
//                result.setResponseCode(ResponseCode.RESPONSE_OK);
//            } else {
//                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//                result.setDescription("The idleMachine update is failed, please try again");
//            }
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The qrCode preBind is failed, please try again");
        }
        return result;
    }

    /**
     * This method is used to check qrcode
     *
     * @return
     */
    @PostMapping(value = "/check")
    public ResultData check(String candidate) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(candidate)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please fill in the first three paragraphs");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("search", new StringBuffer(candidate).append("%").toString());
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System query error, please try again later");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Can't get the matched qrCode with" + candidate + ", please inspect");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<QRCode> list = (List<QRCode>) response.getData();
            if (list.size() > 1) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("Please fill in the third paragraph");
                return result;
            } else {
                condition.clear();
                condition.put("codeValue", candidate);
                response = preBindService.fetch(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    QRCode qrCodeVo = list.get(0);
                    result.setData(qrCodeVo);
                } else {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("The qrCode" + candidate + "has been prebind, please inspect");
                }
            }
        }
        return result;
    }

    /**
     * This method is used to relieve the binding between qrcode and machine
     *
     * @return
     */
    @PostMapping(value = "/prebind/unbind/{codeValue}")
    public ResultData deletePreBind(@PathVariable String codeValue) {
        ResultData result = new ResultData();
        ResultData response = preBindService.deletePreBind(codeValue);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System error, please try again later");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("delete the prebind using code" + codeValue);
        }
        return result;
    }

    /**
     * The method is used to get the prebind list for verify
     *
     * @return
     **/
    @GetMapping(value = "/prebind/list")
    public ResultData getPreBindList(@RequestParam(required = false) String param) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(param)) {
            JSONObject paramJson = JSON.parseObject(param);
            if (!StringUtils.isEmpty(paramJson.get("codeValue"))) {
                condition.put("codeValue", paramJson.getString("codeValue"));
            }
            if (!StringUtils.isEmpty(paramJson.get("machineId"))) {
                condition.put("machineId", paramJson.getString("machineId"));
            }
            if (!StringUtils.isEmpty(paramJson.get("startDate"))) {
                condition.put("startTime", paramJson.getString("startDate"));
            }
            if (!StringUtils.isEmpty(paramJson.get("endDate"))) {
                condition.put("endTime", paramJson.getString("endDate"));
            }
        }
        condition.put("blockFlag", false);
        ResultData response = preBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Prebind is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query error, please try again");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * This method is used to get all batch with model in qrcode
     *
     * @return
     */
    @GetMapping("/batch/list")
    @CrossOrigin
    public ResultData batchList(String modelId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(modelId)) {
            condition.put("modelId", modelId);
        }
        ResultData response = qrCodeService.fetchBatch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    /**
     * This method is used to get model with qrcode
     *
     * @return
     */
    @GetMapping(value = "/model")
    public ResultData getModel(String codeValue) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(codeValue)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure fill all the fields");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        QRCode qrCodeVo = ((List<QRCode>) response.getData()).get(0);
        String modelId = qrCodeVo.getModelId();
        condition.clear();
        condition.put("modelId", modelId);
        condition.put("blockFlag", false);
        response = goodsService.fetchModel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    /**
     * 根据型号ID查询型号的详细信息
     *
     * @param modelId
     * @return
     */
    @GetMapping("/model/detail")
    public ResultData modelInfo(String modelId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供设备的型号");
            return result;
        }
        ResultData response = qrCodeService.modelDetail(modelId);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("根据型号ID查询");
        }
        result.setData(response.getData());
        return result;
    }

    @GetMapping(value = "/findbyqrcode")
    public ResultData findMachineIdByCodeValue(String codeValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = preBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the machineId by qrcode");
        }
        return result;
    }

    //根据codeValue查询code_machine_bind表
    @GetMapping(value = "/findbyqrcode/consumer")
    public ResultData findMachineIdByCodeValueFacetoConsumer(String codeValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the machineId by qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the machineId by qrcode");
        }
        return result;
    }


    //查prebind表,检查machineid 是否存在
    @GetMapping(value = "/check/existmachineid")
    public ResultData checkMachineIdExist(String machineId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineId);
        condition.put("blockFlag", false);
        ResultData response = preBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the machineId!");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the machineId");
        }
        return result;
    }

    //根据codeValue查qrcode表
    @GetMapping(value = "/check/existqrcode")
    public ResultData checkQRcodeExist(String codeValue) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the qrcode by codeValue");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the qrcode.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the qrcode");
        }
        return result;
    }

    //根据codeUrl查qrcode表
    @PostMapping(value = "/probe/byurl")
    public ResultData probeQRcodeByUrl(String codeUrl) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeUrl", codeUrl);
        condition.put("blockFlag", false);
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get the qrcode by code url");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the qrcode by code url");
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the qrcode by code url");
            return result;
        }

    }

    /**
     * 获取设备的在线状态(旧)
     *
     * @param qrcode
     * @return
     */
    //check online
    @GetMapping("/checkonline")
    public ResultData checkOnline(String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the qrcode");
            return result;
        }
        //check whether the qrcode is online
        ResultData response = findMachineIdByCodeValueFacetoConsumer(qrcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find machine id by qrcode in code machine bind table");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find machine id by qrcode in code machine bind table");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
            Map<String, Object> condition = new HashMap<>();
            condition.put("machineId", machineId);
            condition.put("blockFlag", false);
            response = boardVersionService.fetchBoardVersion(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find board version by machineId");
                return result;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to find borad version by machineId");
                return result;
            }
            int version = ((List<BoardVersion>) response.getData()).get(0).getVersion();

            if (version == 1) {
                response = coreV1Service.isOnline(machineId);
            } else if (version == 2) {
                response = coreV2Service.isOnline(machineId);
            } else if (version == 3) {
                response = coreV3Service.isOnline(machineId);
            }

            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("is online");
                return result;
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("is not online");
                return result;
            }
        }
        return result;
    }

    //get prebind list by date now
    @GetMapping(value = "/prebind/list/now")
    public ResultData preBindnow() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        condition.put("dateNow", sdf.format(date));
        ResultData response = preBindService.fetchByDate(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No prebind found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve prebind, please try again later");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}
