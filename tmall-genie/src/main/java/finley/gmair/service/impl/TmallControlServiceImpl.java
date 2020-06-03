package finley.gmair.service.impl;

import com.fasterxml.jackson.annotation.JsonView;
import finley.gmair.model.tmallGenie.*;
import finley.gmair.service.TmallControlService;
import finley.gmair.service.holder.OperationStrategyHolder;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallControlEnum;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo"></a>
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/ozlpg3#ce76a5a4"></a>
 */
@Service
public class TmallControlServiceImpl implements TmallControlService {

    @Autowired
    OperationStrategyHolder operationStrategyHolder;

    @Override
    public AliGenieRe control(Payload payload, Header header) {

        TmallDeviceTypeEnum deviceType = TmallDeviceTypeEnum.valueOf(payload.getDeviceType());

        // 原来实现是通过switch case装配对应的策略，后来发现逻辑太冗余了，改成策略+注解方式
        // 根据设备类型获取对应的操作策略
        OperationStrategy operationStrategy = operationStrategyHolder.getByDeviceType(deviceType);

        ResultData resultData = new ResultData();
        String deviceId = payload.getDeviceId();
        if(operationStrategy != null) {
            String value = payload.getValue();
            // 对设备的控制操作
            TmallControlEnum controlOperation = TmallControlEnum.valueOf(header.getName());
            switch (controlOperation) {
                case TurnOn:
                    resultData = operationStrategy.turnOn(deviceId);
                    break;
                case TurnOff:
                    resultData = operationStrategy.turnOff(deviceId);
                    break;
                case SetWindSpeed:
                    resultData = operationStrategy.setWindSpeed(deviceId, value, false, false);
                    break;
                case AdjustUpWindSpeed:
                    resultData = operationStrategy.setWindSpeed(deviceId, value, true, false);
                    break;
                case AdjustDownVolume:
                    resultData = operationStrategy.setWindSpeed(deviceId, value, false, true);
                    break;
                case OpenSwing:
                    resultData = operationStrategy.openSwing(deviceId);
                    break;
                case CloseSwing:
                    resultData = operationStrategy.closeSwing(deviceId);
                    break;
                default:
                    resultData = null;
                    break;
            }
        }

        AliGenieRe response = new AliGenieRe();
        Payload payloadRes;
        // 如果是不支持的操作/不支持的设备，则resultData == null
        if (resultData != null && resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            header.setName(header.getName().concat("Response"));
            response.setHeader(header);
            payloadRes = new Payload(deviceId);
        } else {
            header.setName("ErrorResponse");
            response.setHeader(header);
            payloadRes = new Payload(deviceId, "DEVICE_NOT_SUPPORT_FUNCTION", "device not support");
        }
        response.setPayload(payloadRes);

        return response;
    }

}
