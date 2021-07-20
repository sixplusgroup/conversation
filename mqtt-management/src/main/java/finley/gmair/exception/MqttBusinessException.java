package finley.gmair.exception;

/**
 * mqtt业务逻辑异常
 *
 * @author lycheeshell
 * @date 2020/12/12 16:07
 */
public class MqttBusinessException extends Exception {

    public MqttBusinessException(String message){
        super(message);
    }

}
