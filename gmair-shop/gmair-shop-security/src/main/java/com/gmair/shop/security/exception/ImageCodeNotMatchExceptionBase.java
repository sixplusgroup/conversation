

package com.gmair.shop.security.exception;

public class ImageCodeNotMatchExceptionBase extends BaseGmairAuth2Exception {

    public ImageCodeNotMatchExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "image_code_not_match";
    }
}
