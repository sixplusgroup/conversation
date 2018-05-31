package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.machine.PreBindForm;
import finley.gmair.form.machine.QRCodeCreateForm;
import finley.gmair.form.machine.QRCodeForm;
import finley.gmair.model.goods.GoodsModel;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.model.machine.QRCode;
import finley.gmair.service.GoodsService;
import finley.gmair.service.IdleMachineService;
import finley.gmair.service.PreBindService;
import finley.gmair.service.QRCodeService;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            result.setData(response.getData());
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
            String filename = generateZip(batch);
            result.setData(filename);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Sorry, the qrCodes are not generated as expected, please try again");
        return result;
    }

    private String generateZip(String batchValue) {
        // judge whether the batch no is illegal, including empty and not exist
        if (StringUtils.isEmpty(batchValue)) {
            return "";
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("batchValue", batchValue);
        ResultData response = qrCodeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return "";
        }
        // read all qrCodes in the batch, generate a zip file
        String base = PathUtil.retrivePath();
        File directory = new File(new StringBuffer(base).append("/material/zip").toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String tempSerial = IDGenerator.generate("ZIP");
        File zip = new File(
                new StringBuffer(base).append("/material/zip/").append(tempSerial).append(".zip").toString());
        if (!zip.exists()) {
            try {
                zip.createNewFile();
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        }
        List<QRCode> list = (List<QRCode>) response.getData();
        File[] files = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            File file = new File(new StringBuffer(PathUtil.retrivePath()).append(list.get(i)).toString());
            files[i] = file;
        }
        return tempSerial;
    }

    @ResponseBody
    @GetMapping(value = "/download/{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        File file = null;
        if (filename.startsWith("ZIP")) {
            filename = filename + ".zip";
            file = new File(PathUtil.retrivePath() + "/material/zip/" + filename);
        }
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
                bufferedOutputStream.write(buff, 0, bytesRead);
            }
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
            Map<String, Object> condition = new HashMap<>();
            condition.put("machineId", form.getMachineId());
            response = idleMachineService.modify(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The idleMachine update is failed, please try again");
            }
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The qrCode preBind is failed, please try again");
        }
        return result;
    }

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

    @PostMapping(value = "/prebind/check")
    public ResultData preBindCheck(String candidate) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(candidate)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please fill in the first three paragraphs");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("search", new StringBuffer(candidate).append("%").toString());
        ResultData response = preBindService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Don't query the matched bind record with" + candidate);
            return result;
        }
        List<PreBindCode> list = (List<PreBindCode>) response.getData();
        if (list.size() > 1) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Please fill in the third paragraph");
            return result;
        }
        PreBindCode preBindCodeVo = list.get(0);
        result.setData(preBindCodeVo);
        return result;
    }

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
}
