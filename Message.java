package serverviewphoto.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private int messageID ;
    // messageID = 1 -> getUserList()
    // messageID = 2 -> activate,vlidate...
    // messageID = 3 -> init SendScreenShot
    // messageID = 4 -> accept screenshot
    // messageId = 0 -> remove screenshot request
    // messageID = 5 -> add user tu online
    // messageID = 6 -> remove online user
    
    public Message(int messageID) {
        this.messageID = messageID;
    }
    
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
