package viewphoto.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import serverviewphoto.messages.ScreenShotMessage;
import viewphoto.GUIClient.ViewPhoto;

public class OnlineUsersController implements Initializable {

    @FXML
    private ListView<String> ListViewOnlineUsers;

    @FXML
    private Button ButtonSendRequest;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setOnlineUsers();
        
        ButtonSendRequest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.sendRequest(new ScreenShotMessage(ViewPhoto.user.getName(), ListViewOnlineUsers.getSelectionModel().getSelectedItem(), ViewPhoto.user.getScreanShotPicturePath(), 4));
                ViewPhoto.user.zatvoriGUI("OnlineUsers", false);
                ViewPhoto.user.prikaziGUI("ViewPhoto");
            }
        });
    }    
    
    public void setOnlineUsers() {
        ArrayList<String> list = ViewPhoto.user.getOnlineUsers();
        for (int i = 0; i < list.size(); i++) {
            ListViewOnlineUsers.getItems().add(list.get(i));
        }
    }
            
}
