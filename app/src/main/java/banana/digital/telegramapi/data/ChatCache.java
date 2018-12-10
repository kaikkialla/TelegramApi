package banana.digital.telegramapi.data;


import org.drinkless.td.libcore.telegram.Client.ResultHandler;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ChatCache implements ResultHandler {

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


    public void emitChatsChangedEvent() {
        EventBus.getDefault().post(new ChatsChangedEvent(mChats));
    }


    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateNewChat.CONSTRUCTOR:
                mChats.add(((TdApi.UpdateNewChat) object).chat);
                emitChatsChangedEvent();
                break;


            case TdApi.UpdateChatLastMessage.CONSTRUCTOR:
                for (TdApi.Chat chat : mChats) {
                    if (chat.id == ((TdApi.UpdateChatLastMessage) object).chatId) {
                        chat.lastMessage = ((TdApi.UpdateChatLastMessage) object).lastMessage;
                        emitChatsChangedEvent();
                    }
                }
                break;
            case TdApi.UpdateFile.CONSTRUCTOR:
                for (TdApi.Chat chat : mChats) {
                    if (chat.photo.small.id == ((TdApi.UpdateFile) object).file.id) {
                        chat.photo.small = ((TdApi.UpdateFile) object).file;
                        emitChatsChangedEvent();
                    }
                }


        }


    }


    public static class ChatsChangedEvent {
        private final List<TdApi.Chat> chats;

        public ChatsChangedEvent(List<TdApi.Chat> chats) {
            this.chats = chats;
        }
    }
}
