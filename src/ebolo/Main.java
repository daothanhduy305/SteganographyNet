package ebolo;

import ebolo.net.listeners.IncomingListener;
import ebolo.net.listeners.OutgoingListener;
import ebolo.ui.controller.MainController;
import ebolo.ui.utils.Announcement;
import ebolo.ui.utils.UIUtils;
import ebolo.utils.Configurations;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = UIUtils.getInstance().createWindow(
                "Steganography Net",
                "MainFXML.fxml",
                MainController.getInstance(),
                200, 75
        );
        primaryStage = mainStage;
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                IncomingListener.getInstance().stopListening();
                OutgoingListener.getInstance().stopListening();
                new Thread(() -> {
                    try {
                        synchronized (Configurations.getInstance()) {
                            Configurations.getInstance().save();
                        }
                    } catch (IOException e) {
                        synchronized (System.out) {
                            System.out.println("Can't save configurations");
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        IncomingListener.getInstance().startListening();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}
