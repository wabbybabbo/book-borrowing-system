package org.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * 业务异常
 */
@NoArgsConstructor
public class ServiceException extends RuntimeException {

    public ServiceException(String msg) {
        super(msg);
    }

}
