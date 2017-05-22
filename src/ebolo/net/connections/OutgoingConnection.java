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
    private String ipAddr;

    public OutgoingConnection(Message message, String ipAddr) {
        this.message = message;
        this.ipAddr = ipAddr;
    }

    @Override
    public void run() {
        try {
            Socket peer = new Socket(ipAddr, Configurations.getPORT());
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
