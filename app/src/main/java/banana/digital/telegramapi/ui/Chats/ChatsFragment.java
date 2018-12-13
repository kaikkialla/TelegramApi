package banana.digital.telegramapi.ui.Chats;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import banana.digital.telegramapi.R;
import banana.digital.telegramapi.data.ChatCache;
import banana.digital.telegramapi.data.TelegramManager;
import banana.digital.telegramapi.ui.ChatFragment.ChatFragment;
import banana.digital.telegramapi.ui.MainActivity;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                TelegramManager.getInstance().getChats();
                adapter.notifyDataSetChanged();

                Handler mHandler = new Handler();//In UI Thread
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

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
        adapter.swap(event.getChats());
    }

}









class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    MainActivity activity;
    RecyclerView recyclerView;
    private final List<TdApi.Chat> mChats = new ArrayList<>();
    String lastMessage;
    String title;

    public Adapter(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.chat_item, viewGroup, false);
        ViewHolder chatsHolder = new ViewHolder(view);
        return chatsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        final TdApi.Chat chat = mChats.get(pos);

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ChatFragment fragment = ChatFragment.newInstance(chat.id);
                activity.showFragment(fragment);
            }
        });

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

    void swap(@NonNull List<TdApi.Chat> chats) {
        mChats.clear();
        mChats.addAll(chats);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return mChats.size();
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

