package viewphoto.GUIClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import serverviewphoto.ServerViewPhoto;
import serverviewphoto.messages.ActivationKey;
import serverviewphoto.messages.Message;
import serverviewphoto.messages.ScreenShotMessage;

public class User {

    private String name;
    private final int TCP_PORT = 1793;
    private final int TCP_MESSAGE_PORT = 1794;
    private Socket soket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private MessageListener messageListener;
    private String screanShotPicturePath;

    public Stage activationStage = new Stage();
    public Stage viewPhotoStage = new Stage();
    public Stage albumCreateStage = new Stage();
    public Stage onlineUsersStage = new Stage();
    public Stage acceptDeclineScreenShotStage = new Stage();
    public Stage aboutStage = new Stage();
    public Stage userGuideStage = new Stage();

    public User() {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            soket = new Socket(addr, TCP_PORT);
            Socket messageSoket = new Socket(addr, TCP_MESSAGE_PORT);
            out = new ObjectOutputStream(soket.getOutputStream());
            in = new ObjectInputStream(soket.getInputStream());
            messageListener = new MessageListener(messageSoket);

            String activation = checkActivation();
            if (activation != null) {
                out.writeObject(new ActivationKey(activation, "", 5));
                String name = (String) in.readObject();
                this.name = name;
                prikaziGUI("ViewPhoto");
                Stage stage = vratiStage("ViewPhoto");
                stage.setTitle(name);
            } else {
                prikaziGUI("Activation");
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreanShotPicturePath() {
        return screanShotPicturePath;
    }

    public void setScreanShotPicturePath(String screanShotPicturePath) {
        this.screanShotPicturePath = screanShotPicturePath;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public Socket getSoket() {
        return soket;
    }

    public void setSoket(Socket soket) {
        this.soket = soket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void zatvoriKonekciju() {
        try {
            out.writeObject(new Message(-1));
            out.close();
            in.close();
            soket.close();
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prikaziGUI(String naziv) {
        try {
            Stage stage = vratiStage(naziv);
            String resurs = "/viewphoto/GUI/" + naziv + ".fxml";
            Pane myPane = (Pane) FXMLLoader.load(getClass().getResource(resurs));
            Scene myScene = new Scene(myPane);
            stage.setTitle(naziv);
            stage.setScene(myScene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(final WindowEvent windowEvent) {
                    try {
                        out.writeObject(new Message(6));
                    } catch (IOException ex) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    zatvoriGUI(naziv, true);
                }
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zatvoriGUI(String naziv, boolean kraj) {
        Stage stage = vratiStage(naziv);
        stage.close();
        if (kraj) {
            zatvoriKonekciju();
        }
    }

    public Stage vratiStage(String naziv) {
        if (naziv.equals("Activation")) {
            return activationStage;
        } else if (naziv.equals("ViewPhoto")) {
            return viewPhotoStage;
        } else if (naziv.equals("AlbumCreate")) {
            return albumCreateStage;
        } else if (naziv.equals("OnlineUsers")) {
            return onlineUsersStage;
        } else if (naziv.equals("AcceptDeclineScreenShot")) {
            return acceptDeclineScreenShotStage;
        } else if (naziv.equals("About")) {
            return aboutStage;
        } else if (naziv.equals("UserGuide")) {
            return userGuideStage;
        }
        return null;
    }

    public boolean checkKey(ActivationKey activationKey) {
        try {
            out.writeObject(activationKey);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ArrayList<String> getOnlineUsers() {
        try {
            out.writeObject(new Message(1));
            return (ArrayList<String>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String checkActivation() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:/activation.txt"));
            String line = br.readLine();
            br.close();
            return line;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void sendRequest(ScreenShotMessage screenShotMessage) {
        try {
            out.writeObject(screenShotMessage);
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveActivation(String key) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("C:/activation.txt")));
            out.write(key);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
