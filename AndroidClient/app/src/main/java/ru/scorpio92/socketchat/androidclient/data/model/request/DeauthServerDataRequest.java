package ru.scorpio92.socketchat.androidclient.data.model.request;

public class DeauthServerDataRequest {

    private String authToken;

    public DeauthServerDataRequest(String authToken) {
        this.authToken = authToken;
    }
}
