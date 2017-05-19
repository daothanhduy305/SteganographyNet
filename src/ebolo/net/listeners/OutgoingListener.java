package ebolo.net.listeners;

import ebolo.net.connections.OutgoingConnection;
import ebolo.net.data.Message;

import java.io.IOException;

/**
 * Created by ebolo on 18/05/2017.
 */
public class OutgoingListener {
    private static OutgoingListener ourInstance;

    public static OutgoingListener getInstance() {
        if (ourInstance == null)
            ourInstance = new OutgoingListener();
        return ourInstance;
    }

    private OutgoingListener() {
    }

    public void send(Message message, String ipAddr) throws IOException {
        new Thread(new OutgoingConnection(message, ipAddr)).start();
    }
}
