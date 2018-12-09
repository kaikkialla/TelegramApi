package banana.digital.telegramapi.data;

import android.content.Context;
import android.support.annotation.NonNull;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ChatCache implements Client.ResultHandler {
    public static ChatCache instance;
    public final List<TdApi.Chat> mChats = new ArrayList<>();


    public static ChatCache getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new ChatCache();
        }
        return instance;
    }

    public void initialize() {
        TelegramManager.getInstance().addResultHandler(this);
    }


    @Override
    public void onResult(TdApi.Object object) {
        if(object.getConstructor() == TdApi.UpdateNewChat.CONSTRUCTOR) {
            mChats.add(((TdApi.UpdateNewChat) object).chat);
            EventBus.getDefault().post(new ChatsChangedEvent(mChats));
        }

    }

    public static class ChatsChangedEvent{
        public final List<TdApi.Chat> chats;
        public ChatsChangedEvent(List<TdApi.Chat> chats) {
            this.chats = chats;
        }
    }
}