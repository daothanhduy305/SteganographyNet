package ebolo.ui.controller;

import ebolo.net.data.Message;
import ebolo.net.data.ui.ReceivedMessageManager;
import ebolo.ui.utils.UIUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

/**
 * Created by ebolo on 19/05/2017.
 */
public class ReceivedMesWindowController {
    private static ReceivedMesWindowController ourInstance;
    private Stage messageWindow;

    @FXML
    private TableView<Message> receivedMesTableView;

    @FXML
    private TableColumn<Message, Integer> idCol;

    @FXML
    private TableColumn<Message, String> timeCol;

    @FXML

    public static ReceivedMesWindowController getInstance() {
        if (ourInstance == null)
            ourInstance = new ReceivedMesWindowController();
        return ourInstance;
    }

    private ReceivedMesWindowController() {
    }

    public void initialize() {
        receivedMesTableView.setItems(ReceivedMessageManager.getInstance().getReceivedMesList());
        idCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                null, "idNumber",
                ReceivedMessageManager
                        .getInstance()
                        .getReceivedMesList()
                        .indexOf(param.getValue()) + 1
        ));
        timeCol.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getDate())
        );
        receivedMesTableView.setRowFactory(param -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    if (messageWindow == null)
                        try {
                            messageWindow = UIUtils.createWindow(
                                    "Message",
                                    "./fxml/MessageWindowFXML.fxml",
                                    MessageWindowController.getInstance(),
                                    500, 350
                            );
                            messageWindow.setOnCloseRequest(event1 ->
                                    MessageWindowController.getInstance().clear());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    MessageWindowController.getInstance().setData(row.getItem());
                    messageWindow.show();
                }
            });
            return row;
        });
    }

    public void closeMessageWindow() {
        if (messageWindow != null)
            messageWindow.close();
    }
}
