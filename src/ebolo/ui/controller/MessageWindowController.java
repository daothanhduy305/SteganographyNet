package ebolo.ui.controller;

import ebolo.crypt.steganography.Steganography;
import ebolo.net.data.Message;
import ebolo.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Created by ebolo on 22/05/2017.
 */
public class MessageWindowController {
    private static MessageWindowController ourInstance;
    private Message message;

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea contentBox;

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
        contentBox.setText("");
    }

    @FXML
    private void reveal() {
        contentBox.setText(
                Steganography.decrypt(message.getEncryptedImage()));
    }
}
