

package com.gmair.shop.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 */
public abstract class GmairAuth2Exception extends AuthenticationException {

	public GmairAuth2Exception(String msg) {
		super(msg);
	}

	public int getHttpErrorCode() {
		// 400 not 401
		return HttpStatus.BAD_REQUEST.value();
	}

	public abstract String getOAuth2ErrorCode();
}
