package serverviewphoto.messages;

public class ActivationKey extends Message {
    
    private  String key ;
    private  String userName ;

    public ActivationKey(String key, String userName, int messageID) {
        super(messageID);
        this.key = key;
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
