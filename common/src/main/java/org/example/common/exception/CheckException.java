package org.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * 参数检查异常
 */
@NoArgsConstructor
public class CheckException extends ServiceException {

    public CheckException(String msg) {
        super(msg);
    }

}
