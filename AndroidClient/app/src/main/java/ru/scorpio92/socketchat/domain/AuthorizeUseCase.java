package ru.scorpio92.socketchat.domain;

import io.reactivex.Observable;
import ru.scorpio92.socketchat.data.model.BaseMessage;
import ru.scorpio92.socketchat.data.model.request.AuthServerDataRequest;
import ru.scorpio92.socketchat.data.repository.network.AuthRepo;
import ru.scorpio92.socketchat.util.JsonWorker;
import ru.scorpio92.socketchat.util.LocalStorage;
import ru.scorpio92.sdk.architecture.domain.rxusecase.RxAbstractUseCase;

public class AuthorizeUseCase extends RxAbstractUseCase<Void> {

    private String login;
    private String password;
    private AuthRepo authRepo;

    public AuthorizeUseCase(String login, String password) {
        this.login = login;
        this.password = password;
        authRepo = new AuthRepo();
    }

    @Override
    protected Observable<Void> buildObservable() throws Exception {
        BaseMessage authMsg = new BaseMessage(BaseMessage.Type.AUTHORIZE);
        authMsg.setServerData(JsonWorker.getSerializeJson(new AuthServerDataRequest(login, password)));
        return authRepo.authorizeAndGetToken(authMsg)
                .toObservable()
                .flatMap(token -> {
                    LocalStorage.getLocalStorageInstance().setDataInFile(LocalStorage.AUTH_TOKEN_STORAGE, token);
                    return Observable.empty();
                });
    }

    @Override
    public void cancel() {
        super.cancel();
        authRepo.cancel();
    }
}
