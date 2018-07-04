package ru.scorpio92.socketchat.authserver.api;

public interface ApiCallback {

    void onApiStart(String apiName);

    void onApiStop(String apiName);

    void onError(Exception e);
}
