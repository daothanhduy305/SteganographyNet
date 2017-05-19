package ebolo.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class MainController {
    @FXML
    private Label mesCountLabel;

    private Stage sendWindow;

    private static MainController ourInstance;

    public static MainController getInstance() {
        if (ourInstance == null)
            ourInstance = new MainController();
        return ourInstance;
    }

    private MainController() {
    }

    @FXML
    private void createNewSendRequest() throws IOException {
        if (sendWindow == null) {
            sendWindow = new Stage();
            FXMLLoader loader  =new FXMLLoader(
                    Paths.get("./fxml/SendWindowFXML.fxml").toUri().toURL());
            loader.setController(SendWindowController.getInstance());
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 500);
            sendWindow.setScene(scene);
        }
        sendWindow.show();
    }
}
