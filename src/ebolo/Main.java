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
