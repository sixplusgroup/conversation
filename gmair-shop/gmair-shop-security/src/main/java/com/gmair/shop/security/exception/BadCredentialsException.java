

package com.gmair.shop.security.exception;

/**
 * 密码不正确
 */
public class BadCredentialsException extends GmairAuth2Exception{
    public BadCredentialsException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "bad_credentials";
    }
}
