package ebolo.ui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by ebolo on 26/05/2017.
 */
public class UIUtils {
    private static UIUtils ourInstance;

    public static UIUtils getInstance() {
        if (ourInstance == null)
            ourInstance = new UIUtils();
        return ourInstance;
    }

    private UIUtils() {
    }

    public Stage createWindow(String name, String fxmlFileName, Object controller,
                                     double width, double height)
            throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/" + fxmlFileName));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(name);
        return stage;
    }
}
