package ebolo.net.data;

import ebolo.crypt.steganography.Steganography;
import ebolo.net.listeners.OutgoingListener;
import ebolo.ui.utils.Announcement;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ebolo on 19/05/2017.
 */
public class MessageFactory {
    public static String createSendingMessage(
            String textFilePath, String imageFilePath, String ipAddr) throws Exception {
        StringBuilder errorCodeBuilder = new StringBuilder("");
        errorCodeBuilder.append(checkFile(textFilePath));
        errorCodeBuilder.append(checkFile(imageFilePath));
        errorCodeBuilder.append(checkIPAddr(ipAddr));
        if (errorCodeBuilder.toString().equals("000")) {
            ImageIcon encryptedImage = Steganography.encryptF(Paths.get(textFilePath),
                    new Image(Paths.get(imageFilePath).toUri().toURL().toString()));
            if (encryptedImage != null) {
                Message message = new Message(encryptedImage);
                OutgoingListener.getInstance().send(message, ipAddr);
            } else
                Announcement.showAnnouncement(
                        "",
                        Alert.AlertType.ERROR,
                        "Encrypt error",
                        "Plain file size is too large!");
        }
        return errorCodeBuilder.toString();
    }

    private static char checkFile(String textFilePath) {
        return (!textFilePath.isEmpty() && (new File(textFilePath)).exists())? '0' : '1';
    }

    private static char checkIPAddr(String ipAddr) {
        return (ipAddr == null
                || ipAddr.isEmpty()
                || !ipAddr.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) ? '1': '0';
    }
}
