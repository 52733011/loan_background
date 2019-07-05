package com.xiaochong.loan.background.exception;

/**
 * Created by ray.liu on 2017/7/19.
 */
public class FileException extends Exception {

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
