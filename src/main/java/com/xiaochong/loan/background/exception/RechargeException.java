package com.xiaochong.loan.background.exception;

/**
 * Created by ray.liu on 2017/7/19.
 */
public class RechargeException extends Exception {

    public RechargeException(String message) {
        super(message);
    }

    public RechargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
