
package viewphoto.GUIClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Platform;
import serverviewphoto.messages.Message;
import serverviewphoto.messages.ScreenShotMessage;


public class MessageListener extends Thread {
    
    private Socket soket;
    private ObjectInputStream in;
    private ScreenShotMessage screenShotMessage;
    
    public MessageListener(Socket soket) {
        this.soket = soket;
        try {
            in = new ObjectInputStream(soket.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        start();
    }
    
     public void run() {
        try {
            boolean petlja = true;
            while (petlja) {
                Message message = (Message) in.readObject();
                if (message.getMessageID() < 0) {
                    petlja = false;
                } else if (message.getMessageID() == 4) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            screenShotMessage = (ScreenShotMessage) message;
                            ViewPhoto.user.prikaziGUI("AcceptDeclineScreenShot");
                        }
                    });
                }
            }
            in.close();
            soket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ScreenShotMessage getScreenShotMessage() {
        return screenShotMessage;
    }

    public void setScreenShotMessage(ScreenShotMessage screenShotMessage) {
        this.screenShotMessage = screenShotMessage;
    }

    public Socket getSoket() {
        return soket;
    }

    public void setSoket(Socket soket) {
        this.soket = soket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }
    
}
