package ebolo.ui.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Created by ebolo on 19/05/2017.
 */
public class Announcement {
    private static final String textFileErrorStr = "Invalid text file";
    private static final String imageFileErrorStr = "Invalid image file";
    private static final String ipAddrErrorStr = "Invalid IP address";

    public static void showAnnouncement(String errorCode, Alert.AlertType alertType,
                                        String header, String... customMessages) {
        StringBuilder errorBuilder = new StringBuilder("");
        if (errorCode.length() == 3 && Integer.parseInt(errorCode) >= 0) {
            if (Integer.parseInt("" + errorCode.toCharArray()[0]) == 1)
                errorBuilder.append(textFileErrorStr + '\n');
            if (Integer.parseInt("" + errorCode.toCharArray()[1]) == 1)
                errorBuilder.append(imageFileErrorStr + '\n');
            if (Integer.parseInt("" + errorCode.toCharArray()[2]) == 1)
                errorBuilder.append(ipAddrErrorStr + '\n');
        } else {
            if (customMessages.length > 0) {
                for (String message : customMessages) {
                    errorBuilder.append(message).append('\n');
                }
            }
        }
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(
                    alertType == Alert.AlertType.ERROR? "Error" :
                            alertType == Alert.AlertType.WARNING? "Warning" :
                                    alertType == Alert.AlertType.INFORMATION? "Information" : ""
            );
            alert.setHeaderText(header);
            alert.setContentText(errorBuilder.toString());
            alert.showAndWait();
        });
    }



}
