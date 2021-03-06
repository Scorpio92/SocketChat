package ru.scorpio92.socketchat.androidclient.presentation.presenter;

import android.support.annotation.NonNull;

import ru.scorpio92.socketchat.R;
import ru.scorpio92.socketchat.androidclient.data.model.RegInfo;
import ru.scorpio92.socketchat.androidclient.data.model.request.RegisterServerDataRequest;
import ru.scorpio92.socketchat.androidclient.domain.RegisterUseCase;
import ru.scorpio92.socketchat.androidclient.exception.reg.InvaidLoginException;
import ru.scorpio92.socketchat.androidclient.exception.reg.InvaidNicknameException;
import ru.scorpio92.socketchat.androidclient.exception.reg.InvaidPasswordException;
import ru.scorpio92.socketchat.androidclient.exception.reg.LoginExistsException;
import ru.scorpio92.socketchat.androidclient.exception.reg.NicknameExistsException;
import ru.scorpio92.socketchat.androidclient.presentation.presenter.base.IRegistrationPresenter;
import ru.scorpio92.socketchat.androidclient.presentation.view.base.IRegistrationFragment;
import ru.scorpio92.socketchat.androidclient.util.Logger;
import ru.scorpio92.socketchat.androidclient.util.ValidateUtils;
import ru.scorpio92.sdk.architecture.domain.rxusecase.SimpleObserver;
import ru.scorpio92.sdk.architecture.presentation.presenter.BasePresenter;

public class RegistrationPresenter extends BasePresenter<IRegistrationFragment> implements IRegistrationPresenter {

    private RegisterUseCase registerUseCase;

    public RegistrationPresenter(@NonNull IRegistrationFragment view) {
        super(view);
    }

    @Override
    public void onLPInput(String login, String password) {
        if(!ValidateUtils.validateParam(login, RegisterServerDataRequest.LOGIN_REGEXP)) {
            getView().onFailedLoginValidation(getView().getViewContext().getString(R.string.invalid_login));
            return;
        }

        if(!ValidateUtils.validateParam(password, RegisterServerDataRequest.PASSWORD_REGEXP)) {
            getView().onFailedPasswordValidation(getView().getViewContext().getString(R.string.invalid_password));
            return;
        }

        getView().onStep2();
    }

    @Override
    public void register(String login, String password, String nickname) {
        if(!ValidateUtils.validateParam(nickname, RegisterServerDataRequest.NICKNAME_REGEXP)) {
            getView().onFailedNicknameValidation(getView().getViewContext().getString(R.string.invalid_nickname));
            return;
        }

        registerUseCase = new RegisterUseCase(new RegInfo(login, password, nickname));
        registerUseCase.execute(new SimpleObserver<Void>() {
            @Override
            protected void onStart() {
                if (checkView())
                    getView().showProgress();
            }

            @Override
            public void onComplete() {
                if (checkView()) {
                    getView().hideProgress();
                    getView().onSuccessRegistration();
                }
            }

            @Override
            public void onError(Throwable e) {
                handleErrors((Exception) e);
            }
        });
    }

    @Override
    protected void onCustomHandleErrors(@NonNull Exception e) {
        super.onCustomHandleErrors(e);
        String error;
        if (e instanceof InvaidLoginException) {
            error = getView().getViewContext().getString(R.string.invalid_login);
        } else if (e instanceof InvaidNicknameException) {
            error = getView().getViewContext().getString(R.string.invalid_nickname);
        } else if (e instanceof InvaidPasswordException) {
            error = getView().getViewContext().getString(R.string.invalid_password);
        } else if (e instanceof LoginExistsException) {
            error = getView().getViewContext().getString(R.string.login_exists);
        } else if (e instanceof NicknameExistsException) {
            error = getView().getViewContext().getString(R.string.nickname_exists);
        } else {
            error = provideDefaultErrorMsg();
        }
        if (checkView())
            getView().onError(error);
    }

    @Override
    protected void writeExceptionInLog(Exception e) {
        Logger.error(e);
    }

    @Override
    protected String provideDefaultErrorMsg() {
        return getView().getViewContext().getString(R.string.wtf_error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registerUseCase != null)
            registerUseCase.cancel();
    }
}
