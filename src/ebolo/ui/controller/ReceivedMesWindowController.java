package ebolo.ui.controller;

import ebolo.net.data.Message;
import ebolo.net.data.ui.ReceivedMessageManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * Created by ebolo on 19/05/2017.
 */
public class ReceivedMesWindowController {
    private static ReceivedMesWindowController ourInstance;

    @FXML
    private ListView<Message> receivedListView;

    public static ReceivedMesWindowController getInstance() {
        if (ourInstance == null)
            ourInstance = new ReceivedMesWindowController();
        return ourInstance;
    }

    private ReceivedMesWindowController() {
    }

    public void initialize() {
        receivedListView.setItems(ReceivedMessageManager.getInstance().getReceivedMesList());
        receivedListView.setCellFactory(param -> new ListCell<Message>(){
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getDate());
                }
            }
        });
    }
}
