

package com.gmair.shop.security.handler;

import cn.hutool.core.util.CharsetUtil;
import com.gmair.shop.security.exception.BaseGmairAuth2Exception;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登陆失败处理
 */
@Component
@Slf4j
public class LoginAuthFailedHandler implements AuthenticationFailureHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) {

        if (!(exception instanceof BaseGmairAuth2Exception)) {
            return;
        }

        BaseGmairAuth2Exception auth2Exception = (BaseGmairAuth2Exception) exception;

        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(auth2Exception.getHttpErrorCode());
        PrintWriter printWriter = response.getWriter();
        printWriter.append(auth2Exception.getMessage());
    }

}
