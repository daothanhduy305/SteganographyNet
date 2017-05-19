package ebolo.net.connections;

import ebolo.net.data.Message;
import ebolo.utils.Configurations;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ebolo on 18/05/2017.
 */
public class OutgoingConnection implements Runnable{
    private Message message;
    private Socket peer;

    public OutgoingConnection(Message message, String ipAddr) throws IOException {
        this.message = message;
        peer = new Socket(ipAddr, Configurations.getPORT());
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(peer.getOutputStream())
            );
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
