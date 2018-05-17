package finley.gmair.dao.impl;

import finley.gmair.dao.CommunicationDao;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.repo.MachineStatusRepository;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommunicationDaoImpl implements CommunicationDao {

    @Autowired
    private MachineStatusRepository machineStatusRepository;

    @Override
    public ResultData insert(MachineStatus status) {
        ResultData result = new ResultData();
        try {
            machineStatusRepository.save(status);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}