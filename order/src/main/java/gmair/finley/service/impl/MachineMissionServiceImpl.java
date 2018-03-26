package gmair.finley.service.impl;

import finley.gmair.model.order.MachineMission;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.MachineMissionVo;
import gmair.finley.dao.MachineMissionDao;
import gmair.finley.service.MachineMissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/17.
 */
@Service
public class MachineMissionServiceImpl implements MachineMissionService {
    private Logger logger = LoggerFactory.getLogger(MachineMissionServiceImpl.class);

    @Autowired
    private MachineMissionDao machineMissionDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = machineMissionDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<MachineMissionVo> list = (List<MachineMissionVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData create(MachineMission machineMission) {
        ResultData result = new ResultData();
        ResultData response = machineMissionDao.insert(machineMission);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
