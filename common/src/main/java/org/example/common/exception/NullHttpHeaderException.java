package org.example.common.exception;

/**
 * 空请求头异常
 */
public class NullHttpHeaderException extends BaseException {

    public NullHttpHeaderException() {
    }

    public NullHttpHeaderException(String msg) {
        super(msg);
    }

}
