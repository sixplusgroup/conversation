

package finley.gmair.exception;


import finley.gmair.enums.membership.MembershipHttpStatus;
import org.springframework.http.HttpStatus;

public class MembershipGlobalException extends RuntimeException{

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
	public MembershipGlobalException(MembershipHttpStatus httpStatus) {
		super(httpStatus.getMsg());
		this.httpStatusCode = httpStatus.value();
	}

	/**
	 * @param httpStatus http状态码
	 */
	public MembershipGlobalException(MembershipHttpStatus httpStatus, String msg) {
		super(msg);
		this.httpStatusCode = httpStatus.value();
	}


	public MembershipGlobalException(String msg) {
		super(msg);
		this.httpStatusCode = HttpStatus.BAD_REQUEST.value();
	}


	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

}
