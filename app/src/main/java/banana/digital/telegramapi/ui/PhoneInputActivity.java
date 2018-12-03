package banana.digital.telegramapi.ui;


import android.app.ActionBar;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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





        Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new CountryRecyclerView()).commit();
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if(Adapter.Country != null) {
            Country.setText(Adapter.Country);
        } else if(Adapter.Country == null){
            Country.setText("Выберите страну");
        }

    }
}

/*TODO
Сделать штуку с отправкой смс
*/