

package com.gmair.shop.security.exception;

/**
 * 密码不正确
 */
public class BadCredentialsExceptionBase extends BaseGmairAuth2Exception {
    public BadCredentialsExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "bad_credentials";
    }
}
