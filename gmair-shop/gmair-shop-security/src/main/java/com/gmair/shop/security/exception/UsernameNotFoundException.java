

package com.gmair.shop.security.exception;

public class UsernameNotFoundException extends BaseGmairAuth2Exception {

    public UsernameNotFoundException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "username_not_found";
    }
}
