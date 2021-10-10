

package com.gmair.shop.security.exception;

import org.springframework.http.HttpStatus;

/**
 */
public class UnauthorizedException extends GmairAuth2Exception {

	public UnauthorizedException(String msg) {
		super(msg);
	}


	public UnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}

}
