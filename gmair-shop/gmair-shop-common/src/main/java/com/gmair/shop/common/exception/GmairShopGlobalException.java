

package com.gmair.shop.common.exception;

import com.gmair.shop.common.enums.GmairHttpStatus;
import org.springframework.http.HttpStatus;

public class GmairShopGlobalException extends RuntimeException{

	/**
	 *
	 */	private static final long serialVersionUID = -4137688758944857209L;

	/**
	 * http状态码
	 */
	private Integer httpStatusCode;


	/**
	 * @param httpStatus http状态码
	 */
	public GmairShopGlobalException(GmairHttpStatus httpStatus) {
		super(httpStatus.getMsg());
		this.httpStatusCode = httpStatus.value();
	}

	/**
	 * @param httpStatus http状态码
	 */
	public GmairShopGlobalException(GmairHttpStatus httpStatus, String msg) {
		super(msg);
		this.httpStatusCode = httpStatus.value();
	}


	public GmairShopGlobalException(String msg) {
		super(msg);
		this.httpStatusCode = HttpStatus.BAD_REQUEST.value();
	}


	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

}
