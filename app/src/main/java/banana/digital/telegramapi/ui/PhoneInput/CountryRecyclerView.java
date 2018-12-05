package banana.digital.telegramapi.ui.PhoneInput;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.ui.MainActivity;

public class CountryRecyclerView extends Fragment {

    RecyclerView recyclerView;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.country_recycler_view_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Adapter adapter = new Adapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));


        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PhoneInputFragment()).commit();
            }
        });
    }

}


class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    MainActivity activity;
    Context context;
    public static String Country;
    public static int Code;

    public Adapter (MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.country_recycler_view_row, parent, false );
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        context = this.context;
        final CountryConstructor countryConstructor = CountryDatabase.Countries[position];
        int CountryCode = countryConstructor.CountryCode;
        String CountryName = countryConstructor.CountryName;

        holder.CountryName.setText(CountryName);
        holder.CountryCode.setText("+" + CountryCode);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Country = countryConstructor.CountryName;
                Code = countryConstructor.CountryCode;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new PhoneInputFragment()).commit();
            }
        });



    }


    @Override
    public int getItemCount() {
        return CountryDatabase.Countries.length;

    }




    class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        Context context;
        TextView CountryName;
        TextView CountryCode;


        public ViewHolder(View v) {
            super(v);
            this.context = context;
            this.v = v;
            CountryName = v.findViewById(R.id.CountryName);
            CountryCode = v.findViewById(R.id.CountryCode);


        }
    }
}