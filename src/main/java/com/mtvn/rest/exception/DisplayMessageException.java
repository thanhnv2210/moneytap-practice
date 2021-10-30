package com.mtvn.rest.exception;

import com.mtvn.enums.error.ErrorCode;

public class DisplayMessageException extends RuntimeException {

    private ErrorCode errorCode;

    public DisplayMessageException(String message) {
        super(message);
    }

    public DisplayMessageException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DisplayMessageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
