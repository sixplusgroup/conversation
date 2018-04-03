package finley.gmair.service.impl;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.model.order.OrderItem;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.dao.ChannelDao;
import finley.gmair.dao.OrderDao;
import finley.gmair.service.LocationService;
import finley.gmair.service.OrderService;
import finley.gmair.util.OrderExtension;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private LocationService locationService;

    @Override
    public ResultData fetchPlatformOrderChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = channelDao.queryChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No channel information found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find channel information");
        }
        return result;
    }

    @Override
    public ResultData createPlatformOrderChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        ResultData response = channelDao.insertChannel(channel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert channel: " + channel.toString());
        }
        return result;
    }

    @Override
    public ResultData updatePlatformOrderChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        ResultData response = channelDao.updateChannel(channel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update channel with channel_id: ").append(channel.getChannelId()).append(" to: ").append(channel.toString()).toString());
        }
        return result;
    }

    @Override
    public ResultData process(MultipartFile file) {
        ResultData result = new ResultData();
        try {
            String filename = file.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf(".") + 1);
            File of = new File(filename);
            file.transferTo(of);
            if (!StringUtils.isEmpty(extension) && !(OrderExtension.XLSX.equals(extension) || OrderExtension.XLS.equals(extension))) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The file extension is not as expected");
                return result;
            }
            Workbook workbook = WorkbookFactory.create(of);

        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    private List<PlatformOrder> process(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            return null;
        }
        List<PlatformOrder> list = new ArrayList<>();
        Row header = sheet.getRow(0);
        int[] index = index(header);
        if (index == null) {
            return null;
        }
        int row = 1;
        Row current = sheet.getRow(row);
        while (current != null) {
            String channel = stringValue(0, index, current);
            String number = stringValue(1, index, current);
            String model = stringValue(2, index, current);
            double quantity = doubleValue(3, index, current);
            Date date = dateValue(4, index, current);
            String username = stringValue(5, index, current);
            String phone = stringValue(6, index, current);
            String address = stringValue(7, index, current);
            String description = stringValue(8, index, current);
            OrderItem item = new OrderItem(model, quantity, 0);
        }
        return null;
    }

    private int[] index(Row row) {
        int[] index = new int[OrderExtension.HEADER.length];
        List<String> list = new ArrayList<>();
        int i = 0;
        Cell cell = row.getCell(i);
        do {
            list.add(cell.getStringCellValue());
            i++;
            cell = row.getCell(i);
        } while (cell != null && !StringUtils.isEmpty(cell.getStringCellValue()));
        for (i = 0; i < index.length; i++) {
            index[i] = list.indexOf(OrderExtension.HEADER[i]);
            if (index[i] < 0) {
                return null;
            }
        }
        return index;
    }

    private String stringValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? "" : row.getCell(index[i]).getStringCellValue();
    }

    private double doubleValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? 0 : row.getCell(index[i]).getNumericCellValue();
    }

    private Date dateValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? Calendar.getInstance().getTime() : row.getCell(index[i]).getDateCellValue();
    }
}
