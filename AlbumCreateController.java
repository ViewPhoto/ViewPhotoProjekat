package viewphoto.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import viewphoto.GUIClient.ViewPhoto;

public class AlbumCreateController implements Initializable {

    @FXML
    private TextField TextfFeldName;

    @FXML
    private Button ButtonCreate;
    
    @FXML
    private Button ButtonCancel;

    @FXML
    private TextField TextfFeldDescription;

    @FXML
    private TextField TextfFeldCreationDate;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ButtonCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = new File(ViewPhoto.folderPath + "/" + TextfFeldName.getText());
                file.mkdirs();
                ViewPhoto.user.zatvoriGUI("AlbumCreate", false);
                ViewPhoto.user.prikaziGUI("ViewPhoto");
            }
        });
        
        ButtonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.zatvoriGUI("AlbumCreate", false);
                ViewPhoto.user.prikaziGUI("ViewPhoto");
            }
        });
    }    
    
}
