

package com.gmair.shop.api.security;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.gmair.shop.common.util.Json;
import com.gmair.shop.security.enums.App;
import com.gmair.shop.security.model.AppConnect;
import com.gmair.shop.security.entity.GmairUser;
import com.gmair.shop.security.service.GmairUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 账号密码登录
 */
@Component
public class WebLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final GmairUserDetailsService gmairUserDetailsService;

    private final WxMaService wxMaService;

    @Autowired
    public WebLoginAuthenticationFilter(GmairUserDetailsService gmairUserDetailsService, WxMaService wxMaService) {
        super("/shop/consumer/webLogin");
        this.gmairUserDetailsService = gmairUserDetailsService;
        this.wxMaService = wxMaService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!ServletUtil.METHOD_POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String requestBody = getStringFromStream(request);

        if (StrUtil.isBlank(requestBody)) {
            throw new AuthenticationServiceException("无法获取输入信息");
        }
        WebAuthenticationToken authentication  =  Json.parseObject(requestBody, WebAuthenticationToken.class);
        String userMail = String.valueOf(authentication.getPrincipal());
        String loginPassword = String.valueOf(authentication.getCredentials());

        GmairUser loadedUser = null;

//        WxMaJscode2SessionResult session = null;

        AppConnect appConnect = new AppConnect();
        appConnect.setAppId(App.H5.value());
        loadedUser = gmairUserDetailsService.loadUserByUserMail(userMail,loginPassword);
//        try {
//
////            session = wxMaService.getUserService().getSessionInfo(code);
//
//            loadedUser = gmairUserDetailsService.loadUserByAppIdAndBizUserId(App.MINI,session.getOpenid());
//        } catch (WxErrorException e) {
//            throw new WxErrorExceptionBase(e.getMessage());
//        } catch (UsernameNotFoundExceptionBase var6) {
//            if (session == null) {
//                throw new WxErrorExceptionBase("无法获取用户登陆信息");
//            }
//            appConnect.setBizUserId(session.getOpenid());
//            appConnect.setBizUnionid(session.getUnionid());
//            gmairUserDetailsService.insertUserIfNecessary(appConnect);
//        }

//        if (loadedUser == null) {
//            loadedUser = gmairUserDetailsService.loadUserByAppIdAndBizUserId(App.MINI, appConnect.getBizUserId());
//        }
//        MiniAppAuthenticationToken result = new MiniAppAuthenticationToken(loadedUser, authentication.getCredentials());
        WebAuthenticationToken result = new WebAuthenticationToken(loadedUser, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }


    private String getStringFromStream(HttpServletRequest req) {
        ServletInputStream is;
        try {
            is = req.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[10240];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0) {
                    nTotalRead = nTotalRead + nRead;
                }
            }
            return new String(bytes, 0, nTotalRead, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }
}
