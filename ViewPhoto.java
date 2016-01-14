package viewphoto.GUIClient;

import javafx.application.Application;
import javafx.stage.Stage;

public class ViewPhoto extends Application {

    public static User user;
    public static String folderPath = "C:/ViewPhoto";
    
    @Override
    public void start(Stage stage) throws Exception {
        user = new User();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
 
