package banana.digital.telegramapi.ui.ChatFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import banana.digital.telegramapi.ui.MainActivity;

public class ChatFragment extends Fragment {

    public static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";
    public Adapter adapter;


    public static ChatFragment newInstance(long chatId) {
        final ChatFragment fragment = new ChatFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_CHAT_ID, chatId);
        fragment.setArguments(arguments);
        return fragment;
    }

    public ChatFragment() {
        // stub
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_fragment_layout, container, false);
        long chatId = getArguments().getLong(EXTRA_CHAT_ID);
        adapter = new Adapter((MainActivity) getActivity(), chatId);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager reversedLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(reversedLayoutManager);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        final long chatId = getArguments().getLong(EXTRA_CHAT_ID);
        TelegramManager.getInstance().requestMessages(chatId, 0);
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
    public void onMessageChangeEvent(ChatCache.MessagesChangedEvent event) {
        adapter.swap(event.getMessages());
    }

}









class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    MainActivity activity;
    long chatId;
    private final List<TdApi.Message> mMessages = new ArrayList<>();

    public Adapter(MainActivity mainActivity, long chatId) {
        this.activity = mainActivity;
        this.chatId = chatId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.message_item, viewGroup, false);
        ViewHolder chatsHolder = new ViewHolder(view);
        return chatsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        final TdApi.Message message = mMessages.get(pos);


        if (message == null) {
            long fromMessageId = mMessages.get(mMessages.size() - 2).id;
            TelegramManager.getInstance().requestMessages(chatId, fromMessageId);
        } else {
            if (message.content instanceof TdApi.MessageText) {
                holder.message.setText(((TdApi.MessageText) message.content).text.text);
            }

            holder.date.setText(String.valueOf(message.date));
        }

/*
        if(pos == ChatCache.getInstance().mChats.size() - 1 ) {
            holder.separator.setVisibility(View.GONE);
        }
        */
/*
        if(chat.lastMessage != null) {
            if (chat.lastMessage.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
                lastMessage = ((TdApi.MessageText) chat.lastMessage.content).text.text;
            }
        }
        */

/*
        title = chat.title;
        if(chat.photo != null) {
            if(chat.photo.small.local.isDownloadingCompleted) {
                Glide.with(activity).load(chat.photo.small.local.path).into(holder.photo);
            } else TelegramManager.getInstance().downloadFile(chat.photo.small.id, 13);
        }
*/


    }


    void swap(@NonNull List<TdApi.Message> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        mMessages.add(null);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.message.setText(null);
        holder.date.setText(null);
    }





class ViewHolder extends RecyclerView.ViewHolder {
    View v;
    Context context;
    TextView message;
    TextView date;

    public ViewHolder(View v) {
        super(v);
        this.context = context;
        this.v = v;
        message = v.findViewById(R.id.message);
        date = v.findViewById(R.id.date);

    }
    public <T>  T randomOfTwo(T o1, T o2) {
        if(Math.random() > .5) {
            return o1;
        } else return o2;}
}

}

