package banana.digital.telegramapi.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.TelegramManager;
import banana.digital.telegramapi.ui.Chats.ChatsFragment;
import banana.digital.telegramapi.ui.CodeInput.CodeInputFragment;
import banana.digital.telegramapi.ui.PhoneInput.PhoneInputFragment;

public class MainActivity extends AppCompatActivity implements Client.ResultHandler{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelegramManager.getInstance().initialize(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        TelegramManager.instance.addResultHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TelegramManager.instance.removeResultHandler(this);
    }


    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onResult(TdApi.Object object) {
        if (object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR) { // если получили UpdateAuthorizationState
            // то вытаскиваем AuthorizationState из UpdateAuthorizationState
            final TdApi.AuthorizationState authorizationState = ((TdApi.UpdateAuthorizationState) object).authorizationState;

            if (authorizationState.getConstructor() == TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR) { // если TDLib запрашивает номер телефона

                showFragment(new PhoneInputFragment());

            } else if (authorizationState.getConstructor() == TdApi.AuthorizationStateWaitCode.CONSTRUCTOR) { // если TDLib запрашивает код

                showFragment(new CodeInputFragment());


            } else if (authorizationState.getConstructor() == TdApi.AuthorizationStateReady.CONSTRUCTOR) { // если всё ок, авторизация прошла успешно


                showFragment(new ChatsFragment());

            }

        }

    }


}
