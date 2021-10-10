

package com.gmair.shop.security.exception;

/**
 * 密码不正确
 */
public class WxErrorExceptionBase extends BaseGmairAuth2Exception {
    public WxErrorExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "wx_error_exception";
    }
}
