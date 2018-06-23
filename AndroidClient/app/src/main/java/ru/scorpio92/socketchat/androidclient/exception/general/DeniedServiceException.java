package ru.scorpio92.socketchat.androidclient.exception.general;

public class DeniedServiceException extends Exception {

    public DeniedServiceException() {
        super("На данный момент сервис недоступен");
    }
}
