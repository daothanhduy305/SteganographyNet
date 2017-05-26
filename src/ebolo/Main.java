package ebolo;

import ebolo.net.listeners.IncomingListener;
import ebolo.net.listeners.OutgoingListener;
import ebolo.ui.controller.MainController;
import ebolo.ui.utils.UIUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage = UIUtils.getInstance().createWindow(
                "Steganography Net",
                "MainFXML.fxml",
                MainController.getInstance(),
                200, 75
        );
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                IncomingListener.getInstance().stopListening();
                OutgoingListener.getInstance().stopListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        IncomingListener.getInstance().startListening();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
