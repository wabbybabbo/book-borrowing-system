package org.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * 已存在异常
 */
@NoArgsConstructor
public class AlreadyExistsException extends BaseException {

    public AlreadyExistsException(String msg) {
        super(msg);
    }

}
