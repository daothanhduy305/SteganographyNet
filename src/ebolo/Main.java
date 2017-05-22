package ebolo;

import ebolo.net.listeners.IncomingListener;
import ebolo.ui.controller.MainController;
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
        FXMLLoader loader = new FXMLLoader(Paths.get("./fxml/MainFXML.fxml").toUri().toURL());
        loader.setController(MainController.getInstance());
        Parent root = loader.load();
        primaryStage.setTitle("Steganography Net");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                IncomingListener.getInstance().stopListening();
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
