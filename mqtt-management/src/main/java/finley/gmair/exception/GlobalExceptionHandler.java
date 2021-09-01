package finley.gmair.exception;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理类
 *
 * @author lycheeshell
 * @date 2020/12/12 16:14
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     *
     * @param e 异常
     * @return 返回给前端的异常提示结果
     */
    @ExceptionHandler(value = Exception.class)
    public ResultData handle(Exception e){
        if (e instanceof MqttBusinessException){
            MqttBusinessException mqttBusinessException = (MqttBusinessException) e;
            return ResultData.error(mqttBusinessException.getMessage());
        } else {
            return ResultData.error(e.getMessage());
        }

    }

}
