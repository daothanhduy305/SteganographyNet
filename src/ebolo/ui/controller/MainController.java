package ebolo.ui.controller;

import ebolo.net.data.ui.ReceivedMessageManager;
import ebolo.ui.utils.UIUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Label mesCountLabel;

    private Stage sendWindow, receivedListWindow;

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
            sendWindow = UIUtils.createWindow(
                    "Create send request",
                    "./fxml/SendWindowFXML.fxml",
                    SendWindowController.getInstance(),
                    500, 500
            );
            sendWindow.setOnCloseRequest(event -> SendWindowController.getInstance().close());
        }
        sendWindow.show();
    }

    @FXML
    private void showReceivedList() throws IOException {
        if (receivedListWindow == null) {
            receivedListWindow = UIUtils.createWindow(
                    "Received list",
                    "./fxml/ReceivedMesWindowFXML.fxml",
                    ReceivedMesWindowController.getInstance(),
                    500, 500
            );
        }
        receivedListWindow.show();
    }

    public void initialize() {
        mesCountLabel.textProperty().bind(Bindings.convert(
                ReceivedMessageManager.getInstance().sizeProperty()));
    }
}
