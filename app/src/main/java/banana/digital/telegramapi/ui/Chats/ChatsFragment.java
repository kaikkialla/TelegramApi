package banana.digital.telegramapi.ui.Chats;

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
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.ChatCache;
import banana.digital.telegramapi.data.TelegramManager;
import banana.digital.telegramapi.ui.MainActivity;
import banana.digital.telegramapi.ui.PhoneInput.PhoneInputFragment;


public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chats_fragment_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Adapter adapter = new Adapter((MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));

    }

    @Override
    public void onStart() {
        super.onStart();
        //подписываемся на изменения чатов
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //отписываемся на изменения чатов
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        TelegramManager.getInstance().getChats();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChatCache.ChatsChangedEvent event) {
        //Отобразить измененные чаты
    };

}









class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    MainActivity activity;
    Context context;

    public Adapter (MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.chats_recycler_view_row, parent, false );
        Adapter.ViewHolder vh = new Adapter.ViewHolder(v);
        return vh;

    }



    @Override
    public void onBindViewHolder(final Adapter.ViewHolder holder, final int position) {
        context = this.context;
        holder.name.setText("number +" + PhoneInputFragment.CountryCode + " " + PhoneInputFragment.PhoneNumber);



    }


    @Override
    public int getItemCount() {
        return 5;

    }




    class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        Context context;
        TextView name;


        public ViewHolder(View v) {
            super(v);
            this.context = context;
            this.v = v;
            name = v.findViewById(R.id.Name);

        }
    }
}

