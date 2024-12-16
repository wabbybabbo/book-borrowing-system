package org.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * 不存在异常
 */
@NoArgsConstructor
public class NotFoundException extends BaseException {

    public NotFoundException(String msg) {
        super(msg);
    }

}
