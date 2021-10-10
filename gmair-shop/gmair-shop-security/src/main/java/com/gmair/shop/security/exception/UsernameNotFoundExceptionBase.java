

package com.gmair.shop.security.exception;

public class UsernameNotFoundExceptionBase extends BaseGmairAuth2Exception {

    public UsernameNotFoundExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "username_not_found";
    }
}
