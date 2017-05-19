package ebolo.net.data.ui;

import ebolo.net.data.Message;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by ebolo on 19/05/2017.
 */
public class ReceivedMessageManager {
    private static ReceivedMessageManager ourInstance;
    private IntegerProperty sizeProp;

    private ObservableList<Message> receivedMesList;

    public static ReceivedMessageManager getInstance() {
        if (ourInstance == null)
            ourInstance = new ReceivedMessageManager();
        return ourInstance;
    }

    private ReceivedMessageManager() {
        receivedMesList = FXCollections.observableArrayList();
        sizeProp = new SimpleIntegerProperty(receivedMesList.size());
    }

    public ObservableList<Message> getReceivedMesList() {
        return receivedMesList;
    }

    public void addNewMessage(Message message) {
        receivedMesList.add(message);
        sizeProp.set(receivedMesList.size());
    }

    public IntegerProperty sizeProperty() {
        return sizeProp;
    }
}
