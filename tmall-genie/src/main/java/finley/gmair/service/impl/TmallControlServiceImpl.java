package finley.gmair.service.impl;

import finley.gmair.model.tmallGenie.AliGenieRe;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.model.tmallGenie.Payload;
import finley.gmair.service.TmallControlService;
import finley.gmair.service.holder.OperationStrategyHolder;
import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallControlEnum;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo"></a>
 * @see <a href="https://www.yuque.com/qw5nze/ga14hc/ozlpg3#ce76a5a4"></a>
 */
@Service
public class TmallControlServiceImpl implements TmallControlService {

    private Logger logger = LoggerFactory.getLogger(TmallControlServiceImpl.class);

    static final String DEVICE_NOT_SUPPORT_FUNCTION = "DEVICE_NOT_SUPPORT_FUNCTION";

    static final String DEVICE_NPT_SUPPORT = "device not support";

    static final String ERROR_RESPONSE = "ErrorResponse";

    @Autowired
    OperationStrategyHolder operationStrategyHolder;

    @Autowired
    private CommonServiceImpl commonServiceImpl;

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

            logger.info("begin control: " + controlOperation);

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
                case AdjustDownWindSpeed:
                    resultData = operationStrategy.setWindSpeed(deviceId, value, false, true);
                    break;
                case OpenSwing:
                    resultData = operationStrategy.openSwing(deviceId);
                    break;
                case CloseSwing:
                    resultData = operationStrategy.closeSwing(deviceId);
                    break;
                case SetMode:
                    resultData = operationStrategy.setMode(deviceId, payload.getValue());
                    break;
                default:
                    resultData = null;
                    break;
            }
        }

        logger.info("after control: " + resultData);

        // 返回结果封装
        AliGenieRe response = new AliGenieRe();
        Payload payloadRes;
        // 如果是不支持的操作/不支持的设备，则resultData == null
        if (resultData != null && resultData.getResponseCode() == ResponseCode.RESPONSE_OK) {
            header.setName(commonServiceImpl.setResponseName(header.getName()));
            response.setHeader(header);
            payloadRes = new Payload(deviceId);
        } else {
            header.setName(ERROR_RESPONSE);
            response.setHeader(header);
            payloadRes = new Payload(deviceId, DEVICE_NOT_SUPPORT_FUNCTION, DEVICE_NPT_SUPPORT);
        }
        response.setPayload(payloadRes);

        return response;
    }

}
