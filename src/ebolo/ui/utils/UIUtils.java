package ebolo.ui.utils;

import com.sun.istack.internal.Nullable;
import ebolo.Main;
import ebolo.utils.Configurations;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

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
        stage.initOwner(Main.getMainStage());
        stage.setScene(scene);
        stage.setTitle(name);
        return stage;
    }

    public String chooseFile(@Nullable String name, @Nullable Window owner,
                            @Nullable FileChooser.ExtensionFilter filter) {
        FileChooser fileChooser = new FileChooser();
        synchronized (Configurations.getInstance()) {
            if (Configurations.getInstance().getLastUsedPath() != null)
                fileChooser.setInitialDirectory(
                        new File(Configurations.getInstance().getLastUsedPath())
                );
        }
        if (filter != null)
            fileChooser.getExtensionFilters().add(filter);
        if (name != null)
            fileChooser.setTitle(name);
        File file = fileChooser.showOpenDialog(owner == null? Main.getMainStage() : owner);
        if (file != null) {
            synchronized (Configurations.getInstance()) {
                Configurations.getInstance().setLastUsedPath(
                        (new StringBuilder(file.getPath())
                                .delete(
                                        file.getPath().lastIndexOf(File.separatorChar) + 1,
                                        file.getPath().length())
                        ).toString()
                );
            }
            return file.getPath();
        }
        return "";
    }
}
