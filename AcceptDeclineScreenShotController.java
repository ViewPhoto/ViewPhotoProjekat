package viewphoto.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import serverviewphoto.messages.ScreenShotMessage;
import viewphoto.GUIClient.ViewPhoto;

public class AcceptDeclineScreenShotController implements Initializable {

    @FXML
    private Button ButtonDecline;

    @FXML
    private Button ButtonAccept;

    @FXML
    private TextField TtextFieldUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        TtextFieldUser.setText(ViewPhoto.user.getMessageListener().getScreenShotMessage().getSender());
        
        ButtonAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                acceptPicture();
                ViewPhoto.user.zatvoriGUI("AcceptDeclineScreenShot", false);
            }
        });
        
        ButtonDecline.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.zatvoriGUI("AcceptDeclineScreenShot", false);
            }
        });
    }    
    
    public void acceptPicture() {
        ScreenShotMessage message = ViewPhoto.user.getMessageListener().getScreenShotMessage();
        String filePath = message.getPath().substring(0, message.getPath().lastIndexOf("\\"));
        String sourceName = message.getPath().substring(message.getPath().lastIndexOf("\\") + 1);
        try{
            File fajlZaKopiranje = new File(filePath, sourceName);
            File kopiraniFajl = new File(filePath, "accept_" + sourceName);
            FileInputStream fis = new FileInputStream(fajlZaKopiranje);
            FileOutputStream fos = new FileOutputStream(kopiraniFajl);
            
            byte[] b = new byte[100];
            int brojBajtova;
            while((brojBajtova = fis.read(b)) > 0){
                fos.write(b, 0, brojBajtova);
            }
            fis.close();
            fos.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
