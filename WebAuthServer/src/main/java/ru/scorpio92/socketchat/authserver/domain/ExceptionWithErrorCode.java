package ru.scorpio92.socketchat.authserver.domain;

public class ExceptionWithErrorCode extends Exception {

    private int errorCode;

    public ExceptionWithErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
