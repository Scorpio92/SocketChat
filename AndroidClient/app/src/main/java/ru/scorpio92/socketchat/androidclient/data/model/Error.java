package ru.scorpio92.socketchat.androidclient.data.model;

public class Error {

    private String errorCode;
    private String message;

    public int getErrorCode() {
        return Integer.valueOf(errorCode);
    }

    public String getMessage() {
        return message;
    }
}
