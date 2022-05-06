package com.navi.my.money.exception;

public class MyMoneyServiceException extends Exception{

    public MyMoneyServiceException(Throwable ex) {
        super(ex);
    }

    public MyMoneyServiceException(String message, Throwable ex) {
        super(message, ex);
    }
}
