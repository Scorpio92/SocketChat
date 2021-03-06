package ru.scorpio92.socketchat.androidclient.data.repository.network;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import ru.scorpio92.socketchat.BuildConfig;
import ru.scorpio92.socketchat.androidclient.data.model.BaseMessage;
import ru.scorpio92.socketchat.androidclient.data.model.response.AuthServerDataResponse;
import ru.scorpio92.socketchat.androidclient.data.repository.network.base.IAuthRepo;
import ru.scorpio92.socketchat.androidclient.data.repository.network.core.RetrofitNetworkRepository;
import ru.scorpio92.socketchat.androidclient.exception.auth.IncorrectPairException;
import ru.scorpio92.socketchat.androidclient.exception.general.WtfException;
import ru.scorpio92.socketchat.androidclient.exception.reg.InvaidLoginException;
import ru.scorpio92.socketchat.androidclient.exception.reg.InvaidNicknameException;
import ru.scorpio92.socketchat.androidclient.exception.reg.InvaidPasswordException;
import ru.scorpio92.socketchat.androidclient.exception.reg.LoginExistsException;
import ru.scorpio92.socketchat.androidclient.exception.reg.NicknameExistsException;
import ru.scorpio92.socketchat.androidclient.util.JsonWorker;

public class AuthRepo extends RetrofitNetworkRepository<API> implements IAuthRepo {

    @Override
    public Completable register(BaseMessage regMsg) {
        return getApiInterface().register(regMsg)
                .flatMapCompletable(baseMessage -> {
                    switch (baseMessage.getStatus()) {
                        case SUCCESS:
                            return Completable.complete();
                        case ERROR:
                            switch (baseMessage.getError().getErrorCode()) {
                                case 1:
                                    throw new InvaidLoginException();
                                case 2:
                                    throw new LoginExistsException();
                                case 3:
                                    throw new InvaidPasswordException();
                                case 4:
                                    throw new InvaidNicknameException();
                                case 5:
                                    throw new NicknameExistsException();
                                default:
                                    throw new WtfException();
                            }
                        default:
                            throw new WtfException();
                    }
                });
    }

    @Override
    public Single<String> authorizeAndGetToken(BaseMessage authMsg) {
        return getApiInterface().authorize(authMsg)
                .flatMap(baseMessage -> {
                    switch (baseMessage.getStatus()) {
                        case SUCCESS:
                            AuthServerDataResponse serverDataResponse = JsonWorker.getDeserializeJson(baseMessage.getServerData(), AuthServerDataResponse.class);
                            return Single.just(serverDataResponse.getAuthToken());
                        case ERROR:
                            switch (baseMessage.getError().getErrorCode()) {
                                case 1:
                                    throw new InvaidLoginException();
                                case 2:
                                    throw new InvaidPasswordException();
                                case 3:
                                    throw new IncorrectPairException();
                                default:
                                    throw new WtfException();
                            }
                            default:
                                throw new WtfException();
                    }
                });
    }

    @Override
    protected API createApiInterface(Retrofit client) {
        return client.create(API.class);
    }

    @Override
    protected String provideBaseURL() {
        return BuildConfig.AUTH_SERVER_IP + ":" + BuildConfig.AUTH_SERVER_PORT;
    }
}
