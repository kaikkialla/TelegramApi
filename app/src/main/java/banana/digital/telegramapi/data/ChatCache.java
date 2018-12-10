package banana.digital.telegramapi.data;


import org.drinkless.td.libcore.telegram.Client.ResultHandler;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ChatCache implements ResultHandler{

    private static ChatCache sInstance;

    public final List<TdApi.Chat> mChats = new ArrayList<TdApi.Chat>();

    public static ChatCache getInstance() {
        if (sInstance == null) {
            sInstance = new ChatCache();
        }
        return sInstance;
    }

    public void initialize() {
        TelegramManager.getInstance().addResultHandler(this);
    }


    @Override
    public void onResult(TdApi.Object object) {
        if (object.getConstructor() == TdApi.UpdateNewChat.CONSTRUCTOR) {
            mChats.add(((TdApi.UpdateNewChat) object).chat);

            EventBus.getDefault().post(new ChatsChangedEvent(mChats));
        }
    }

    public static class ChatsChangedEvent {
        private final List<TdApi.Chat> chats;

        public ChatsChangedEvent(List<TdApi.Chat> chats) {
            this.chats = chats;
        }
    }
}
