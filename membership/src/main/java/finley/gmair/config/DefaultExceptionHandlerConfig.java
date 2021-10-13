

package finley.gmair.config;


import finley.gmair.exception.MembershipGlobalException;
import finley.gmair.util.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 自定义错误处理器
 */
@Controller
@RestControllerAdvice
public class DefaultExceptionHandlerConfig {

    //处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
    @ExceptionHandler(BindException.class)
    public ResponseData<String> bindExceptionHandler(BindException e){
        e.printStackTrace();
        return ResponseData.error(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        //e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
    }
    //处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        e.printStackTrace();
        return ResponseData.error(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        //e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
    }
    //处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseData<String> ConstraintViolationExceptionHandler(ConstraintViolationException e){
        e.printStackTrace();
        return ResponseData.error(e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining()));
    }

    @ExceptionHandler(MembershipGlobalException.class)
    public ResponseData<String> unauthorizedExceptionHandler(MembershipGlobalException e){
        e.printStackTrace();
        return ResponseData.error(e.getMessage());
    }
}