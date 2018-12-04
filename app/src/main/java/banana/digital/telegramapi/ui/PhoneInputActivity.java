package banana.digital.telegramapi.ui;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.drinkless.td.libcore.telegram.Client;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.TelegramManager;


public class PhoneInputActivity extends Fragment {

    Context context;
    TextView Country;//Выбор страны
    TextInputEditText countryCodeEt;
    TextInputEditText phoneNumber;

    String firstPhoneNumberPart;
    String secondPhoneNumberPart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_input_activity, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        Country = view.findViewById(R.id.Country);
        countryCodeEt = view.findViewById(R.id.CountryCodeET);
        phoneNumber = view.findViewById(R.id.PhoneNumber);


        int maxLength = 10 + 2;
        phoneNumber.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});


        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence editable, int i, int i1, int i2) {
                if((editable.length() <= 3)) {
                    
                    firstPhoneNumberPart = String.valueOf(editable);
                    Log.e("tig", firstPhoneNumberPart);
                } else if ((editable.length() > 3)  && (editable.length() <= 7)) {

                    secondPhoneNumberPart = String.valueOf(editable);
                    Log.e("tig", firstPhoneNumberPart + " " + secondPhoneNumberPart.substring(3, secondPhoneNumberPart.length()));
                } else if(editable.length() > 7) {

                    Log.e("tig", firstPhoneNumberPart + " " + secondPhoneNumberPart.substring(3, secondPhoneNumberPart.length()));
                }
            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                /*
                Spannable text = new SpannableString(editable);
                text.setSpan(new ForegroundColorSpan(Color.GREEN), 0, editable.length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                */
            }
        });


        Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new CountryRecyclerView()).commit();
            }
        });


        phoneNumber.setSelection(phoneNumber.getText().length());
        countryCodeEt.setSelection(countryCodeEt.getText().length());
    }


    @Override
    public void onResume() {
        super.onResume();
        if(Adapter.Country != null) {
            Country.setText(Adapter.Country);
            countryCodeEt.setText("+" + String.valueOf(Adapter.Code));
            //Toast.makeText(context, Adapter.CountryCode, Toast.LENGTH_SHORT).show();
        } else if(Adapter.Country == null){
            Country.setText("Выберите страну");
            countryCodeEt.setText("aaa");
        }




    }
}

@SuppressLint("AppCompatCustomView")
class CustomEditText extends EditText {


    public CustomEditText(Context context) {
        super(context);
    }



    @Override
    public void onSelectionChanged(int start, int end) {

        CharSequence text = getText();
        if (text != null) {
            if (start != text.length() || end != text.length()) {
                setSelection(text.length(), text.length());
                return;
            }
        }

        super.onSelectionChanged(start, end);
    }



}
/*TODO
Сделать штуку с отправкой смс
*/