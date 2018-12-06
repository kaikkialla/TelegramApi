package banana.digital.telegramapi.ui.Chats;


import java.io.Serializable;

public class ChatConstructor implements Serializable {
    String CompanionName;
    String LastMessage;



    public ChatConstructor(String CompanionName, String LastMessage){
        this.CompanionName = CompanionName;
        this.LastMessage = LastMessage;


    }


    public String getCompanionName(){
        return CompanionName;
    }

    public String getLastMessage(){
        return LastMessage;
    }


}