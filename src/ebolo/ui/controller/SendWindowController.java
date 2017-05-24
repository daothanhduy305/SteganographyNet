package ebolo.ui.controller;

import com.sun.istack.internal.Nullable;
import ebolo.net.data.MessageFactory;
import ebolo.ui.utils.Announcement;
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

    private String chooseFile(@Nullable String name, FileChooser.ExtensionFilter filter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter);
        if (name != null)
            fileChooser.setTitle(name);
        File file = fileChooser.showOpenDialog(textFilePathField.getScene().getWindow());
        if (file != null) {
            return file.getPath();
        }
        return "";
    }

    @FXML
    private void showTextFileChooser() {
        textFilePathField.setText(chooseFile(
                "Choose text file",
                new FileChooser.ExtensionFilter("Text file", "*.txt")
        ));
    }

    @FXML
    private void showImageFileChooser() {
        imageFilePathField.setText(chooseFile(
                "Choose image file",
                new FileChooser.ExtensionFilter(
                        "Image file",
                        "*.png", "*.jpg", "*.jpeg"
                )
        ));
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
