package finley.gmair.service;

import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ConsumerQRcodeBindService {
    ResultData createConsumerQRcodeBind(ConsumerQRcodeBind consumerQRcodeBind);

    ResultData fetchConsumerQRcodeBind(Map<String, Object> condition);

    ResultData modifyConsumerQRcodeBind(Map<String, Object> condition);

    ResultData queryMachineListView(Map<String, Object> condition);

    ResultData queryMachineSecondListView(Map<String, Object> condition);
}
