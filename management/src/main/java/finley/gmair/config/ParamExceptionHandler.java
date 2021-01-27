package finley.gmair.config;

import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author: Bright Chan
 * @date: 2021/1/11 16:38
 * @description: @Validated校验出错的全局处理类
 */

@RestControllerAdvice
public class ParamExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(ParamExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultData handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return handleParamException(e);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultData handleConstraintViolationException(ConstraintViolationException e) {
        return handleParamException(e);
    }

    private ResultData handleParamException(Exception e) {
        logger.error("[ERROR]: method argument not valid: " + e.getMessage());
        ResultData res = new ResultData();
        res.setResponseCode(ResponseCode.RESPONSE_ERROR);
        res.setDescription("method argument(s) not valid!");
        return res;
    }
}
