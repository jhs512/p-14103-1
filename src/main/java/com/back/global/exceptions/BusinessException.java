package com.back.global.exceptions;

import lombok.Getter;

public class BusinessException extends RuntimeException {
    @Getter
    private final String resultCode;
    @Getter
    private final String msg;

    public BusinessException(String resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }
}
