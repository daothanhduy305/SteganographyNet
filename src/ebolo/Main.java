package ebolo;

import ebolo.net.listeners.ButlerListener;
import ebolo.ui.controller.MainController;
import ebolo.ui.utils.UIUtils;
import javafx.application.Application;
import javafx.stage.Stage;

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

        primaryStage.setOnCloseRequest(event -> ButlerListener.getInstance().stopListening());
        ButlerListener.getInstance().prepare();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}
