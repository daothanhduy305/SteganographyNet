package ebolo.ui.controller;

import com.sun.istack.internal.Nullable;
import ebolo.net.data.MessageFactory;
import ebolo.ui.utils.Announcement;
import ebolo.ui.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Created by ebolo on 18/05/2017.
 */
public class SendWindowController {
    private static SendWindowController ourInstance;

    @FXML
    private TextField textFilePathField, imageFilePathField, ipAddrField;

    public static SendWindowController getInstance() {
        if (ourInstance == null)
            ourInstance = new SendWindowController();
        return ourInstance;
    }

    private SendWindowController() {
    }



    @FXML
    private void showPlainFileChooser() {
        String plainFilePath = UIUtils.getInstance().chooseFile(
                "Choose plain file", MainController.getInstance().getSendWindow(),
                null
        );
        textFilePathField.setText(plainFilePath.isEmpty()? textFilePathField.getText() : plainFilePath);
    }

    @FXML
    private void showImageFileChooser() {
        String imageFilePath = UIUtils.getInstance().chooseFile(
                "Choose image file", MainController.getInstance().getSendWindow(),
                new FileChooser.ExtensionFilter(
                        "Image file",
                        "*.png", "*.jpg", "*.jpeg"
                )
        );
        imageFilePathField.setText(imageFilePath.isEmpty()? imageFilePathField.getText() : imageFilePath);
    }

    @FXML
    private void send() throws Exception {
        try {
            String errorCode = MessageFactory.createSendingMessage(
                    textFilePathField.getText(),
                    imageFilePathField.getText(),
                    ipAddrField.getText()
            );
            if (!errorCode.equals("000"))
                Announcement.showAnnouncement(
                        errorCode, Alert.AlertType.ERROR, "Cannot send message");
            else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        textFilePathField.setText("");
        imageFilePathField.setText("");
        ipAddrField.setText("");
        textFilePathField.getScene().getWindow().hide();
    }
}
