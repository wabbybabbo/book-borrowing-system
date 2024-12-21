package org.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * 操作拒绝异常
 */
@NoArgsConstructor
public class NotAllowedException extends ServiceException {

    public NotAllowedException(String msg) {
        super(msg);
    }

}
