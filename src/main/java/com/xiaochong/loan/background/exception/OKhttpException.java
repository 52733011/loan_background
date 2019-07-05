package com.xiaochong.loan.background.exception;

/**
 * Created by ray.liu on 2017/4/13.
 */
public class OKhttpException extends Exception {

    public OKhttpException(String message) {
        super(message);
    }

    public OKhttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
