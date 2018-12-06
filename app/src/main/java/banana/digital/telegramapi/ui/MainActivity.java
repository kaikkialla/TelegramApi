package banana.digital.telegramapi.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.TelegramManager;
import banana.digital.telegramapi.ui.CodeInput.CodeInputFragment;
import banana.digital.telegramapi.ui.PhoneInput.PhoneInputFragment;

public class MainActivity extends AppCompatActivity implements Client.ResultHandler{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelegramManager.getInstance().initialize(this);

       /* if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PhoneInputFragment()).commit();
        }*/


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


    @Override
    public void onResult(TdApi.Object object) {
        if(object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR) {
            final TdApi.AuthorizationState authorizationState = ((TdApi.UpdateAuthorizationState) object).authorizationState;
            if(authorizationState.getConstructor() == TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PhoneInputFragment()).commit();
            } else if(authorizationState.getConstructor() == TdApi.AuthorizationStateWaitCode.CONSTRUCTOR) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new CodeInputFragment()).commit();
            }
        }
    }
}
