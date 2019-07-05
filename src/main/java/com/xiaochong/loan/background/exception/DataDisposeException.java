package com.xiaochong.loan.background.exception;

/**
 * Created by ray.liu on 2017/4/13.
 */
public class DataDisposeException extends Exception{

    public DataDisposeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDisposeException(String message) {
        super(message);
    }
}
