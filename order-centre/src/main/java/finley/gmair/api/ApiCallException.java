package finley.gmair.api;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-22 14:13
 * @description ：
 */

public class ApiCallException extends Exception {
    public ApiCallException(String message) {
        super(message);
    }

    public ApiCallException(String message, Throwable throwable) {
        super(message,throwable);
    }
}
