package ebolo.ui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by ebolo on 19/05/2017.
 */
public class UIUtils {
    public static Stage createWindow(String name, String fxmlPath, Object controller,
                                     double width, double height)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(Paths.get(fxmlPath).toUri().toURL());
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(name);
        return stage;
    }
}
