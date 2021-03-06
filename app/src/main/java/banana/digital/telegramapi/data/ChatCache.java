package banana.digital.telegramapi.data;


import android.os.Build;
import android.support.annotation.RequiresApi;

import org.drinkless.td.libcore.telegram.Client.ResultHandler;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatCache implements ResultHandler {

    private static ChatCache sInstance;

    public final List<TdApi.Chat> mChats = new ArrayList<TdApi.Chat>();
    public final Map<Long, Map<Long, TdApi.Message>> mMessages = new HashMap<>();


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


    public void emitMessagesChangedEvent(long chatId, List<TdApi.Message> messages) {
        EventBus.getDefault().post(new MessagesChangedEvent(chatId,  messages));
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
                break;


            case TdApi.Messages.CONSTRUCTOR:
                final TdApi.Message[] newMessages = ((TdApi.Messages) object).messages;
                if(newMessages.length > 0) {//Если список новых сообщений не пустой
                    final  long chatId = newMessages[0].chatId;//То для всех новых сообщений будет один chatId
                    Map<Long, TdApi.Message> oldMessages = mMessages.get(chatId);
                    if(oldMessages == null) {
                        oldMessages = new HashMap<>();
                        mMessages.put(chatId, oldMessages);
                    }
                    for(TdApi.Message newMessage : newMessages) {
                        oldMessages.put(newMessage.id, newMessage);
                    }
                    //Collections.addAll(oldMessages, newMessages);


                    final List<TdApi.Message> resultMessages = new ArrayList<>();
                    for(TdApi.Message message : oldMessages.values()) {
                        resultMessages.add(message);
                    }


                    Collections.sort(resultMessages, new Comparator<TdApi.Message>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public int compare(TdApi.Message m1, TdApi.Message m2) {

                            return Long.compare(m2.id, m1.id);
                        }
                    });

                    emitMessagesChangedEvent(chatId, resultMessages);
                }


                break;
            case TdApi.UpdateNewMessage.CONSTRUCTOR:
                break;


        }


    }


    public static class ChatsChangedEvent {
        private final List<TdApi.Chat> chats;
        public ChatsChangedEvent(List<TdApi.Chat> chats) {
            this.chats = chats;
        }
        public List<TdApi.Chat> getChats() {
            return chats;
        }
    }



    public static class MessagesChangedEvent {
        private List<TdApi.Message> messages;
        public long chatId;

        public MessagesChangedEvent(long chatId, List<TdApi.Message> messages) {
            this.messages = messages;
            this.chatId = chatId;
        }

        public long getChatId() {
            return chatId;
        }

        public List<TdApi.Message> getMessages() {
            return messages;
        }
    }
}
