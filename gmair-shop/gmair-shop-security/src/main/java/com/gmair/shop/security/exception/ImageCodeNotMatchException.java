
package com.gmair.shop.security.exception;

public class ImageCodeNotMatchException extends GmairAuth2Exception {

    public ImageCodeNotMatchException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "image_code_not_match";
    }
}
