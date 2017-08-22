package com.shsxt.crm.exception;

@SuppressWarnings("serial")
public class AuthException extends RuntimeException {

    private int errorCode;

    public AuthException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }




}
