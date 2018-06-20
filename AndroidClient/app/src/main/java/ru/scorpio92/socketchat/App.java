package ru.scorpio92.socketchat;

import android.app.Application;

import ru.scorpio92.socketchat.util.LocalStorage;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalStorage.initLocalStorage(getApplicationContext());
    }
}
