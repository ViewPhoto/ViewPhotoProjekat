package viewphoto.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import serverviewphoto.messages.ActivationKey;
import viewphoto.GUIClient.ViewPhoto;

public class ActivationController implements Initializable {

    @FXML
    private Button ButtonActivate;

    @FXML
    private TextField TextFieldName;

    @FXML
    private TextField TextFieldKey;

    @FXML
    private Label LabelIncorectData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        LabelIncorectData.setVisible(false);
        
        ButtonActivate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean valid = ViewPhoto.user.checkKey(new ActivationKey(TextFieldKey.getText(), TextFieldName.getText(), 2));
                if (valid) {
                    ViewPhoto.user.saveActivation(TextFieldKey.getText());
                    ViewPhoto.user.zatvoriGUI("Activation", false);
                    ViewPhoto.user.prikaziGUI("ViewPhoto");
                } else {
                    LabelIncorectData.setVisible(true);
                }
            }
        });
        
    }    
    
}
