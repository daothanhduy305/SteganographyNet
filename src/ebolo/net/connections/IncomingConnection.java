package ebolo.net.connections;

import ebolo.net.data.Message;

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
            //TODO: do something with the message
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
