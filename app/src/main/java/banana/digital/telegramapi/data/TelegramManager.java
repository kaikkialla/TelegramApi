package banana.digital.telegramapi.data;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Locale;

import banana.digital.telegramapi.BuildConfig;
import banana.digital.telegramapi.ConstantValues;

public class TelegramManager implements Client.ExceptionHandler, Client.ResultHandler{

    private static TelegramManager instance;
    private Context mContext;
    public Client mClient;


    public TelegramManager(Context context) {
        mContext = context;
    }

    public static TelegramManager getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new TelegramManager(context);
        }
        return instance;
    }

    public void initialize() {
        mClient = Client.create(this,this,this);
    }

    public void onUpdateAuthorizationState(TdApi.AuthorizationState authorizationState) {
        switch (authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                final TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
                parameters.apiId = ConstantValues.API_ID;
                parameters.apiHash = ConstantValues.API_HASH;
                parameters.enableStorageOptimizer = true;
                parameters.useMessageDatabase = true;//Разрешаем кэшировать чаты и сообщения
                parameters.useFileDatabase = true;//Разрешаем кэшировать файлы
                parameters.filesDirectory =mContext.getExternalFilesDir(null).getAbsolutePath() + "/";//Путь к файлам
                parameters.databaseDirectory = mContext.getExternalFilesDir(null).getAbsolutePath() + "/";//Пусть к базе данных
                parameters.systemVersion = Build.VERSION.RELEASE;//Версия ос
                parameters.deviceModel = Build.DEVICE;//Модель устройства
                parameters.systemLanguageCode = Locale.getDefault().getLanguage();
                parameters.applicationVersion = BuildConfig.VERSION_NAME;
                mClient.send(new TdApi.SetTdlibParameters(parameters), this);
                break;
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                mClient.send(new TdApi.SetDatabaseEncryptionKey(), this);
        }
    }

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR: ;
                //onUpdateAuthorizationState(TdApi.AuthorizationState);
                break;
            case TdApi.UpdateCall.CONSTRUCTOR:
                //TODO
                break;
            case TdApi.UpdateChatLastMessage.CONSTRUCTOR://Новое сообщение
                //TODO
                break;
        }
    }

    @Override
    public void onException(Throwable e) {

    }

}
