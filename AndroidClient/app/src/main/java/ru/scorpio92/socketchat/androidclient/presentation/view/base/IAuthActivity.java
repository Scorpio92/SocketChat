package ru.scorpio92.socketchat.androidclient.presentation.view.base;

import ru.scorpio92.sdk.architecture.presentation.view.IBaseView;

public interface IAuthActivity extends IBaseView {

    void onSuccessRegistration();

    void onSuccessAuth();
}
