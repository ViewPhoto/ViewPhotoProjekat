package serverviewphoto.messages;


public class ScreenShotMessage extends Message {
    
    private String sender ;
    private String reciever ;
    private String path ;

    public ScreenShotMessage(String sender, String reciever, String path, int messageID) {
        super(messageID);
        this.sender = sender;
        this.reciever = reciever;
        this.path = path;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
