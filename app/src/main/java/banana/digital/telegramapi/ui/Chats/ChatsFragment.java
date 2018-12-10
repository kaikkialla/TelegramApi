package banana.digital.telegramapi.ui.Chats;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.ChatCache;
import banana.digital.telegramapi.data.TelegramManager;
import banana.digital.telegramapi.ui.MainActivity;
import banana.digital.telegramapi.ui.PhoneInput.PhoneInputFragment;


public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    Context context;
    Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.chats_fragment_layout, container, false);

        adapter = new Adapter((MainActivity) getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TelegramManager.getInstance().getChats();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatsChangeEvent(ChatCache.ChatsChangedEvent event) {
        adapter.notifyDataSetChanged();
    }

}









class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    MainActivity activity;
    RecyclerView recyclerView;

    String lastMessage;
    String title;

    public Adapter(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.chats_recycler_view_row, viewGroup, false);
        ViewHolder chatsHolder = new ViewHolder(view);
        return chatsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        TdApi.Chat chat = ChatCache.getInstance().mChats.get(pos);

        if(pos == ChatCache.getInstance().mChats.size() - 1 ) {
            holder.separator.setVisibility(View.GONE);
        }

        if(chat.lastMessage != null) {
            if (chat.lastMessage.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
                lastMessage = ((TdApi.MessageText) chat.lastMessage.content).text.text;
            }
        }


        title = chat.title;
        if(chat.photo != null) {
            if(chat.photo.small.local.isDownloadingCompleted) {
                Glide.with(activity).load(chat.photo.small.local.path).into(holder.photo);
            } else TelegramManager.getInstance().downloadFile(chat.photo.small.id, 13);
        }


        holder.name.setText(title);
        holder.lastMessage.setText(String.valueOf(lastMessage));

    }




    @Override
    public int getItemCount() {
        return ChatCache.getInstance().mChats.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        title = null;
        lastMessage = null;
    }






    class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        Context context;
        TextView name;
        TextView lastMessage;
        ImageView photo;
        ImageView separator;


        public ViewHolder(View v) {
            super(v);
            this.context = context;
            this.v = v;
            name = v.findViewById(R.id.Name);
            lastMessage = v.findViewById(R.id.LastMessage);
            photo = v.findViewById(R.id.photo);
            separator = v.findViewById(R.id.bottom_item_separator);

        }
    }
}

