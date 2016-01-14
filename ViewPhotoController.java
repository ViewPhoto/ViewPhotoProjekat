package viewphoto.Controller;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import viewphoto.GUIClient.ViewPhoto;

public class ViewPhotoController implements Initializable {

    @FXML
    private MenuItem Delete;

    @FXML
    private MenuItem AddPicture;

    @FXML
    private ListView<String> ListViewFiles;

    @FXML
    private MenuItem DeleteAlbum;

    @FXML
    private MenuItem NewAlbum;

    @FXML
    private MenuItem RemovePicture;

    @FXML
    private MenuItem About;

    @FXML
    private MenuItem CreateScreenShot;

    @FXML
    private MenuItem UserGuide;

    @FXML
    private ImageView ImageViewPreview;

    @FXML
    private MenuItem Copy;

    @FXML
    private MenuItem Move;

    @FXML
    private MenuItem SlideShow;

    @FXML
    private MenuItem Close;

    @FXML
    private MenuItem Open;

    @FXML
    private TreeView<String> TreeViewFolder;

    @FXML
    private MenuItem SendScreenShot;

    private ArrayList<String> extension = new ArrayList<>();

    private File filePath = null;
    private boolean runSlideShow = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populateTreeView();
        ListViewFiles.setVisible(false);

        TreeViewFolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isFile()) {
                    try {
                        ListViewFiles.setVisible(false);
                        ImageViewPreview.setVisible(true);
                        FileInputStream fp = new FileInputStream(file.getAbsolutePath());
                        ImageViewPreview.setImage(new Image(fp));
                        fp.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    ListViewFiles.setVisible(true);
                    ImageViewPreview.setVisible(false);
                    ListViewFiles.getItems().clear();
                    String files[] = file.list();
                    ListViewFiles.getItems().addAll(files);
                    filePath = file;
                }
            }
        });

        ListViewFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    File file = new File(filePath.getAbsolutePath() + "/" + ListViewFiles.getSelectionModel().getSelectedItem());
                    if (file.isFile()) {
                        ListViewFiles.setVisible(false);
                        ImageViewPreview.setVisible(true);
                        FileInputStream fp = new FileInputStream(file);
                        ImageViewPreview.setImage(new Image(fp));
                        fp.close();
                    } else {
                        filePath = file;
                        ListViewFiles.getItems().clear();
                        ListViewFiles.getItems().addAll(file.list());
                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        CreateScreenShot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    BufferedImage screencapture = new Robot().createScreenCapture(
                            new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                    File file = new File(ViewPhoto.folderPath + "/screencapture.png");
                    ImageIO.write(screencapture, "png", file);
                    populateTreeView();
                } catch (AWTException | IOException ex) {
                    Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        SendScreenShot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                ViewPhoto.user.setScreanShotPicturePath(file.getAbsolutePath());
                ViewPhoto.user.zatvoriGUI("ViewPhoto", false);
                ViewPhoto.user.prikaziGUI("OnlineUsers");
            }
        });

        NewAlbum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.zatvoriGUI("ViewPhoto", false);
                ViewPhoto.user.prikaziGUI("AlbumCreate");
            }
        });

        AddPicture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Find picture");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                Window primaryStage = null;
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile != null) {
                    TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                    File file = new File(ViewPhoto.folderPath + getPath(item) + "/" + selectedFile.getName());
                    try {
                        Files.copy(selectedFile.toPath(), file.toPath(), REPLACE_EXISTING);
                        populateTreeView();
                    } catch (IOException ex) {
                        Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        RemovePicture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isFile()) {
                    ImageViewPreview.setImage(null);
                    file.delete();
                    populateTreeView();

                }
            }
        });

        DeleteAlbum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isDirectory()) {
                    ListViewFiles.setVisible(false);
                    file.delete();
                    populateTreeView();
                }
            }
        });

        Delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                ImageViewPreview.setVisible(false);
                ListViewFiles.setVisible(false);
                file.delete();
                populateTreeView();
            }
        });

        SlideShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isDirectory()) {
                    if (runSlideShow) {
                        runSlideShow = false;
                        return;
                    }
                    final Task task2 = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            File files[] = file.listFiles();
                            runSlideShow = true;
                            ListViewFiles.setVisible(false);
                            ImageViewPreview.setVisible(true);
                            for (int i = 0; i < files.length && runSlideShow; i++) {
                                File file1 = files[i];
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ViewPhoto.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            ImageViewPreview.setImage(new Image(new FileInputStream(file1)));
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });
                            }
                            return null;
                        }
                    };
                    new Thread(task2).start();
                }
            }
        });

        Close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.zatvoriGUI("ViewPhoto", true);
            }
        });

        Copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isFile()) {
                    File newFile = new File(file.getParent() + "\\Copy_" + file.getName());
                    try {
                        Files.copy(file.toPath(), newFile.toPath(), REPLACE_EXISTING);
                        populateTreeView();
                    } catch (IOException ex) {
                        Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        Move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isFile()) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Move picture");
                    fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                    fileChooser.setInitialFileName(file.getName());
                    File selectedFile = fileChooser.showSaveDialog(null);
                    if (selectedFile != null) {
                        try {
                            Files.copy(file.toPath(), selectedFile.toPath(), REPLACE_EXISTING);
                            file.delete();
                            ImageViewPreview.setVisible(false);
                            populateTreeView();
                        } catch (IOException ex) {
                            Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        About.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.prikaziGUI("About");
            }
        });

        UserGuide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewPhoto.user.prikaziGUI("UserGuide");
            }
        });

        Open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TreeItem<String> item = TreeViewFolder.getSelectionModel().getSelectedItem();
                File file = new File(ViewPhoto.folderPath + getPath(item));
                if (file.isFile()) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    public String getPath(TreeItem<String> item) {
        if (item != null && item.getParent() != null) {
            return getPath(item.getParent()) + "\\" + item.getValue();
        }
        return "";
    }

    public void populateTreeView() {
        File file = new File(ViewPhoto.folderPath);
        TreeItem<String> root = null;
        try {
            root = new TreeItem<>(file.getName(), new ImageView(new Image(
                    getClass().getResourceAsStream("/viewphoto/Controller/folder.png"))));
        } catch (Exception ex) {
            Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeViewFolder.setRoot(root);
        createTree(root, file);
    }

    public void createTree(TreeItem<String> child, File path) {
        File file = new File(path.getAbsolutePath());
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isHidden()) {
                TreeItem<String> itemChild = null;
                try {
                    if (files[i].isDirectory()) {
                        itemChild = new TreeItem<>(files[i].getName(), new ImageView(
                                new Image(
                                        getClass().getResourceAsStream("/viewphoto/Controller/folder.png"))));
                    } else {
                        itemChild = new TreeItem<>(files[i].getName());
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ViewPhotoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                itemChild.setExpanded(false);
                child.getChildren().add(itemChild);
                if (files[i].isDirectory()) {
                    createTree(itemChild, files[i]);
                }
            }
        }
    }

}
