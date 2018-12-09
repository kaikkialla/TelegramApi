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
import banana.digital.telegramapi.ui.CodeInput.CodeInputFragment;


public class PhoneInputFragment extends Fragment {

    Context context;
    TextView CountryTv;//Выбор страны
    TextInputEditText countryCodeEt;
    TextInputEditText phoneNumberEt;
    ImageView backButton;
    ImageView applyButton;

    String firstPhoneNumberPart;
    String secondPhoneNumberPart;
    public static int CountryCode;
    public static long PhoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_input_fragment_layout, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        CountryTv = view.findViewById(R.id.Country);
        countryCodeEt = view.findViewById(R.id.CountryCodeET);
        phoneNumberEt = view.findViewById(R.id.PhoneNumber);
        applyButton = view.findViewById(R.id.applyButton);
        backButton = view.findViewById(R.id.backButton);


        int maxLength = 10 + 2;
        phoneNumberEt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});


        countryCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i1, int i2) {
                for(int a = 0; a < 13; a++) {
                    if(String.valueOf(editable).replace(" ", "").equals(String.valueOf(CountryDatabase.Countries[a].CountryCode).replace(" ", ""))) {
                        CountryTv.setText(CountryDatabase.Countries[a].CountryName);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



/*Пытаюсь форматировать номер телефона
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


                //Spannable text = new SpannableString(editable);
                //text.setSpan(new ForegroundColorSpan(Color.GREEN), 0, editable.length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

            }
        });
*/
        CountryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout, new CountryRecyclerView()).commit();
            }
        });



        //phoneNumberEt.setSelection(phoneNumberEt.getText().length());
        //countryCodeEt.setSelection(countryCodeEt.getText().length());


        //Если поля не пустые и формат данных верный, то обрабатываем
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryCode = Integer.parseInt(String.valueOf(countryCodeEt.getText()));
                PhoneNumber = Long.parseLong(String.valueOf(phoneNumberEt.getText()));
                //Проверяем код и номер телефона на правильность
                TelegramManager.getInstance().sendPhoneNumber("+" + String.valueOf(countryCodeEt.getText()) + String.valueOf(phoneNumberEt.getText()));

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new CodeInputFragment()).commit();

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(Adapter.Country != null) {
            CountryTv.setText(Adapter.Country);
            countryCodeEt.setText(String.valueOf(Adapter.Code));
        } else if(Adapter.Country == null){
            CountryTv.setText("Выберите страну");
        }




    }
}

