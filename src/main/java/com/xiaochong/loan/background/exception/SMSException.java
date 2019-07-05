package com.xiaochong.loan.background.exception;

/**
 * Created by yehao on 17/6/16.
 */
public class SMSException extends Exception {

    private static final long serialVersionUID = -1490383232470634300L;

    public SMSException() {
    }

    public SMSException(String message) {
        super(message);
    }
}
