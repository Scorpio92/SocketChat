package ru.scorpio92.socketchat.androidclient.presentation.view.activity;

import android.support.annotation.NonNull;

import ru.scorpio92.sdk.architecture.presentation.presenter.IBasePresenter;
import ru.scorpio92.sdk.architecture.presentation.view.BaseActivity;

public class MainActivity extends BaseActivity {
    @NonNull
    @Override
    protected IBasePresenter providePresenter() {
        return null;
    }
}
