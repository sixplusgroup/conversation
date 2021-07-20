package finley.gmair;

import finley.gmair.vo.ApiResult;
import finley.gmair.dto.BizException;
import finley.gmair.dto.ErrorCode;
import finley.gmair.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lyy
 * @date 2020-07-05 11:19 下午
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult handle(Exception e) {
        //如果是自定义异常
        if (e instanceof BizException) {
            BizException exception = (BizException) e;
            log.error(exception.getMessage());
            return ResultUtil.error(exception.getCode(), exception.getMessage());
        } else {
            log.error("【系统异常】", e);
            return ResultUtil.error(ErrorCode.UNKNOWN_ERROR.getCode(), e.getMessage());
        }
    }
}