package org.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * 缺少参数值异常
 */
@NoArgsConstructor
public class MissingValueException extends ServiceException {

    public MissingValueException(String msg) {
        super(msg);
    }

}
