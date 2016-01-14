package viewphoto.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import viewphoto.GUIClient.ViewPhoto;

public class UserGuideController implements Initializable {

    @FXML
    private Button ButtonClose;

    @FXML
    private TextArea TexxtArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ButtonClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.zatvoriGUI("UsrGuide", false);
            }
        });
    }    
    
}
