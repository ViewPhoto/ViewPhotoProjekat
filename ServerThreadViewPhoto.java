package serverviewphoto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverviewphoto.messages.ActivationKey;
import serverviewphoto.messages.Message;
import serverviewphoto.messages.ScreenShotMessage;

public class ServerThreadViewPhoto extends Thread {

    private Socket socket;
    private String userName;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket messageSocket;
    private ObjectOutputStream messageOut;

    public ServerThreadViewPhoto(Socket socket1, Socket socket2) {
        this.socket = socket1;
        this.messageSocket = socket2;
        try {
            in = new ObjectInputStream(this.socket.getInputStream());
            out = new ObjectOutputStream(this.socket.getOutputStream());
            messageOut = new ObjectOutputStream(this.messageSocket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        try {
            boolean petlja = true;
            while (petlja) {
                Message message = (Message) in.readObject();
                if (message.getMessageID() < 0) {
                    petlja = false;
                    messageOut.writeObject(message);
                } else if (message.getMessageID() == 2) {
                    boolean information = ServerViewPhoto.checkKey((ActivationKey) message);
                    ServerViewPhoto.saveUser((ActivationKey) message);
                    String name = ServerViewPhoto.addOnlineUser((ActivationKey) message);
                    userName = name;
                    out.writeObject(information);
                } else if (message.getMessageID() == 5) {
                    String name = ServerViewPhoto.addOnlineUser((ActivationKey) message);
                    userName = name;
                    out.writeObject(name);
                } else if (message.getMessageID() == 4) {
                    ServerViewPhoto.sendMessageToUser((ScreenShotMessage) message);
                } else if (message.getMessageID() == 6) {
                    ServerViewPhoto.removeOnlineUser(userName);
                } else if (message.getMessageID() == 1) {
                    ArrayList<String> list = ServerViewPhoto.getOnlineUser(); //da mu vratimo listu korisnika
                    list.remove(userName); //iz liste izbacimo sebe
                    out.writeObject(list);
                }
            }
            in.close();
            out.close();
            messageOut.close();
            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerThreadViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ObjectOutputStream getMessageOut() {
        return messageOut;
    }

    public void setMessageOut(ObjectOutputStream messageOut) {
        this.messageOut = messageOut;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }
    
    
}
