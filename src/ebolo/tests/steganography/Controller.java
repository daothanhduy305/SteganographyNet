package ebolo.tests.steganography;

import ebolo.crypt.Steganography;
import ebolo.utils.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by ebolo on 17/05/2017.
 */
public class Controller {
    @FXML
    private ImageView originalImageView, encryptedImageView;
    @FXML
    private Label messageLabel;

    @FXML
    private void encrypt() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Images",
                        "*.png",
                        "*.jpg",
                        "*.jpeg")
        );
        File originalImageFile = fileChooser.showOpenDialog(
                originalImageView.getScene().getWindow()
        );

        if (originalImageFile != null) {
            Image originalImage = new Image(originalImageFile.toURI().toURL().toString());
            originalImageView.setImage(originalImage);
            ImageIcon encryptedImage = Steganography.encrypt("Con cho Suen", originalImage);
            encryptedImageView.setImage(ImageUtils.imageIconToFX(encryptedImage));
            messageLabel.setText(Steganography.decrypt(encryptedImage));
        }
    }
}
