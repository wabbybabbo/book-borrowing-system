package org.example.common.exception;

/**
 * 操作拒绝异常
 */
public class NotAllowedException extends BaseException {

    public NotAllowedException(String msg) {
        super(msg);
    }

}
