package banana.digital.telegramapi.ui.PhoneInput;
/*
С кодом страны:
    Ставим левый паддинг(нижняя полоска останется на метсе, скролл доступен)
    Поверх рисуем плюс
*/

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.TelegramManager;


public class PhoneInputFragment extends Fragment {

    Context context;
    TextView Country;//Выбор страны
    TextInputEditText countryCodeEt;
    TextInputEditText phoneNumberEt;
    ImageView backButton;
    ImageView applyButton;

    String firstPhoneNumberPart;
    String secondPhoneNumberPart;
    public static long PhoneNumber;
    public static int CountryCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_input_fragment_layout, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        Country = view.findViewById(R.id.Country);
        countryCodeEt = view.findViewById(R.id.CountryCodeET);
        phoneNumberEt = view.findViewById(R.id.PhoneNumber);
        applyButton = view.findViewById(R.id.applyButton);
        backButton = view.findViewById(R.id.backButton);


        int maxLength = 10 + 2;
        phoneNumberEt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});





        phoneNumberEt.addTextChangedListener(new TextWatcher() {
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


        phoneNumberEt.setSelection(phoneNumberEt.getText().length());
        countryCodeEt.setSelection(countryCodeEt.getText().length());


        //Если поля не пустые и формат данных верный, то обрабатываем
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryCode = Integer.parseInt(String.valueOf(countryCodeEt.getText()));
                PhoneNumber = Integer.parseInt(String.valueOf(phoneNumberEt.getText()));
                TelegramManager.getInstance(getActivity()).sendPhoneNumber("+" + String.valueOf(countryCodeEt.getText()) + String.valueOf(phoneNumberEt.getText()));
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(Adapter.Country != null) {
            Country.setText(Adapter.Country);
            countryCodeEt.setText(String.valueOf(Adapter.Code));
        } else if(Adapter.Country == null){
            Country.setText("Выберите страну");
        }




    }
}

