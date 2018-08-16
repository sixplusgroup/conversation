package finley.gmair.dao.impl;

import finley.gmair.dao.CommunicationDao;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.repo.MachinePartialStatusRepository;
import finley.gmair.repo.MachineStatusRepository;
import finley.gmair.repo.MachineV1StatusRepository;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommunicationDaoImpl implements CommunicationDao {

    @Autowired
    private MachineV1StatusRepository machineV1StatusRepository;

    @Autowired
    private MachineStatusRepository machineStatusRepository;

    @Autowired
    private MachinePartialStatusRepository machinePartialStatusRepository;

    @Override
    public ResultData insertV1(MachineV1Status status){
        ResultData result = new ResultData();
        try{
            machineV1StatusRepository.save(status);
        }catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }


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

    @Override
    public ResultData insert(MachinePartialStatus status) {
        ResultData result = new ResultData();
        try {
            machinePartialStatusRepository.save(status);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
