package ebolo.net.connections;

import ebolo.net.data.Message;
import ebolo.net.data.ui.ReceivedMessageManager;
import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by ebolo on 18/05/2017.
 */
public class IncomingConnection implements Runnable {
    private Socket peer;

    public IncomingConnection(Socket socket) {
        this.peer = socket;
    }


    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new BufferedInputStream(peer.getInputStream())
            );
            Message incomingMessage = (Message) objectInputStream.readObject();
            Platform.runLater(
                    () -> ReceivedMessageManager.getInstance().addNewMessage(incomingMessage));
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
