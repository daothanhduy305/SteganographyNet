package ebolo.net.connections;

import ebolo.net.data.Message;
import ebolo.ui.utils.ErrorDisplay;
import ebolo.utils.Configurations;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;

/**
 * Created by ebolo on 18/05/2017.
 */
public class OutgoingConnection implements Callable<Boolean>{
    private Message message;
    private String ipAddr;

    public OutgoingConnection(Message message, String ipAddr) {
        this.message = message;
        this.ipAddr = ipAddr;
    }

    @Override
    public Boolean call() {
        try {
            Socket peer = new Socket(ipAddr, Configurations.getPORT());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(peer.getOutputStream())
            );
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (IOException e) {
            if (e instanceof SocketException)
                ErrorDisplay.showError("",
                        ipAddr + " is unreachable!");
            else
                e.printStackTrace();
            return false;
        }
    }
}
