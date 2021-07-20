package finley.gmair.controller;

import finley.gmair.model.log.UserMachineOperationLog;
import finley.gmair.model.maintenance.UserActionHistoryDTO;
import finley.gmair.service.LogService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户操作历史的交互接口
 *
 * @author lycheeshell
 * @date 2021/1/18 23:18
 */
@RestController
@RequestMapping("/maintenance/history")
public class HistoryController {

    @Resource
    private LogService logService;

    /**
     * 查询用户设备的操作历史
     *
     * @param consumerId 用户电话
     * @param qrcode     设备二维码
     * @param pageIndex    第几页
     * @param pageSize     页大小
     * @return 用户设备操作历史
     */
    @GetMapping(value = "/getOperationHistory")
    public ResultData getOperationHistory(String consumerId, String qrcode, Integer pageIndex, Integer pageSize) {
        if (StringUtils.isEmpty(consumerId)) {
            return ResultData.error("consumerId为空");
        }
        if (StringUtils.isEmpty(qrcode)) {
            return ResultData.error("qrcode为空");
        }
        if (pageIndex == null || pageIndex < 0) {
            return ResultData.error("pageIndex为非法");
        }
        if (pageSize == null || pageSize < 0) {
            return ResultData.error("pageSize为非法");
        }

        ResultData result = new ResultData();

        ResultData response = logService.getUserActionLog(consumerId, qrcode, pageIndex, pageSize);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            UserActionHistoryDTO userActionHistoryDTO = new UserActionHistoryDTO();
            List<UserMachineOperationLog> actions = (List<UserMachineOperationLog>) response.getData();
            userActionHistoryDTO.setHistory(actions);

            ResultData totalResponse = logService.getUserActionLog(consumerId, qrcode, 1, Integer.MAX_VALUE);
            if (totalResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<UserMachineOperationLog> totalActions = (List<UserMachineOperationLog>) totalResponse.getData();
                userActionHistoryDTO.setTotal(totalActions.size());

                result.setData(userActionHistoryDTO);
            } else {
                result.setResponseCode(totalResponse.getResponseCode());
                result.setDescription(totalResponse.getDescription());
                return result;
            }
        }

        result.setResponseCode(response.getResponseCode());
        result.setDescription(response.getDescription());

        return result;
    }

}
