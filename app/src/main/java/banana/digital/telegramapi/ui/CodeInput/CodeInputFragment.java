package banana.digital.telegramapi.ui.CodeInput;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.ui.PhoneInput.PhoneInputFragment;

public class CodeInputFragment extends Fragment {

    TextView expirationTime;
    TextView targetPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.code_input_fragment_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expirationTime = view.findViewById(R.id.expirationTime);
        targetPhoneNumber = view.findViewById(R.id.targetPhoneNumber);

        targetPhoneNumber.setText("We will send you a message with verification code on number +" + PhoneInputFragment.CountryCode + " " + PhoneInputFragment.PhoneNumber);


        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished / 1000 % 60 < 10 && millisUntilFinished / 1000 % 60 >= 0) {
                    expirationTime.setText("Telegram will call you in " + millisUntilFinished / 1000 / 60 + ":0" + millisUntilFinished / 1000 % 60);
                } else if(millisUntilFinished / 1000 % 60 >= 10 && millisUntilFinished / 1000 % 60 <= 60) {
                    expirationTime.setText("Telegram will call you in " + millisUntilFinished / 1000 / 60 + ":" + millisUntilFinished / 1000 % 60);
                }
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //Звоним
            }

        }.start();
    }


}
