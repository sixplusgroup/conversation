package finley.gmair.controller;

import finley.gmair.model.preparation.PreBindField;
import finley.gmair.service.PreparationService;
import finley.gmair.util.PreparationExtension;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/management/preparation")
public class PreparationController {

    @Autowired
    private PreparationService preparationService;

    @PostMapping("/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        try {
            MultipartFile file = request.getFile("preBind_list");
            String filename = file.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf(".") + 1);
            if (!StringUtils.isEmpty(extension) && !(PreparationExtension.XLSX.equals(extension) || PreparationExtension.XLS.equals(extension))) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The file extension is not expected");
                return result;
            }
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Please make sure the data is in the #1 sheet");
                return result;
            }
            Row header = sheet.getRow(0);
            List<String> list = index(header);
            if (list == null) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Please make sure that you use the expected template");
                return result;
            }
            //start thread to bind machine,version and codeValue
            new Thread(() ->{
                List<PreBindField> preBindFieldList = process(workbook);
                for (PreBindField item : preBindFieldList) {
                    preparationService.bindVersion(item.getMachineId(), item.getVersion(), item.getCodeValue());
                }
            }).start();

        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    private List<String> index(Row row) {
        List<String> list = new ArrayList<>();
        String cell0_value = row.getCell(0).getStringCellValue();
        list.add(cell0_value);
        String cell1_value = row.getCell(1).getStringCellValue();
        list.add(cell1_value);
        String cell2_value = row.getCell(2).getStringCellValue();
        list.add(cell2_value);
        return list;
    }

    private List<PreBindField> process(Workbook workbook) {
        List<PreBindField> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        int row = 1;
        Row current = sheet.getRow(row);
        while (current != null) {
            String machineId = current.getCell(0).getStringCellValue();
            int version = Integer.parseInt(current.getCell(1).getStringCellValue());
            String codeValue = current.getCell(2).getStringCellValue();
            PreBindField item = new PreBindField(machineId, version, codeValue);
            list.add(item);
            current = sheet.getRow(++row);
        }
        return list;
    }
}
