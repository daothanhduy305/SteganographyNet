package ebolo.ui.controller;

import ebolo.crypt.steganography.Steganography;
import ebolo.net.data.Message;
import ebolo.ui.utils.Announcement;
import ebolo.utils.Configurations;
import ebolo.utils.ImageUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

/**
 * Created by ebolo on 22/05/2017.
 */
public class MessageWindowController {
    private static MessageWindowController ourInstance;
    private Message message;

    @FXML
    private ImageView imageView;

    public static MessageWindowController getInstance() {
        if (ourInstance == null)
            ourInstance = new MessageWindowController();
        return ourInstance;
    }

    private MessageWindowController() {
    }

    public void setData(Message message) {
        this.message = message;
        imageView.setImage(ImageUtils.imageIconToFX(message.getEncryptedImage()));
    }

    public void clear() {
        imageView.setImage(null);
    }

    @FXML
    private void reveal() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        synchronized (Configurations.getInstance()) {
            directoryChooser.setInitialDirectory(
                    new File(Configurations.getInstance().getLastUsedPath()));
        }
        File directory = directoryChooser.showDialog(imageView.getScene().getWindow());
        if (directory != null) {
            StringBuilder fileNameBuilder = new StringBuilder("");
            new Thread(() -> {
                try {
                    Steganography.decryptF(
                            message.getEncryptedImage(),
                            fileNameBuilder
                                    .append(directory.getPath())
                                    .append('/')
                                    .append(message.getDate()
                                            .replace('/', '_')
                                            .replace(' ', '_')
                                            .replace(':', '_')
                                    ).toString()
                    );
                    Platform.runLater(() -> {
                        clear();
                        ReceivedMesWindowController.getInstance().closeMessageWindow();
                        Announcement.showAnnouncement(
                                "",
                                Alert.AlertType.INFORMATION,
                                "Decrypt successfully!",
                                "File is saved as ",
                                fileNameBuilder.toString()
                        );
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
