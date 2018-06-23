package ru.scorpio92.socketchat.androidclient.data.model.request;

public class AuthServerDataRequest {

    private String login;
    private String password;

    public AuthServerDataRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
