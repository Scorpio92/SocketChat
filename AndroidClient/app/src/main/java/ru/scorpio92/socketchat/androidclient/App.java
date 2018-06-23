package ru.scorpio92.socketchat.androidclient;

import android.app.Application;

import ru.scorpio92.socketchat.androidclient.util.LocalStorage;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalStorage.initLocalStorage(getApplicationContext());
    }
}
