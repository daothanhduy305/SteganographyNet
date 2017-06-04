package ebolo.net.connections;

import ebolo.net.data.Message;
import ebolo.ui.utils.Announcement;
import ebolo.utils.Configurations;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
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
            Socket peer;
            //Scan for Butler
            for (int tryingPort = 49152; tryingPort < 65535; tryingPort++) {
                peer = new Socket(ipAddr, tryingPort);
                ObjectInputStream stream = new ObjectInputStream(
                        new BufferedInputStream(
                                peer.getInputStream()
                        )
                );
                String response = stream.readUTF();
                stream.close();
                peer.close();
                System.out.println("Got direction as: " + response);
                String[] args = response.split("\\s");
                if (args.length == 2) {
                    if (args[0].equals("Steganography")) {
                        try {
                            int port = Integer.parseInt(args[1]);
                            peer = new Socket(ipAddr, port);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                                    new BufferedOutputStream(peer.getOutputStream())
                            );
                            objectOutputStream.writeObject(message);
                            objectOutputStream.flush();
                            objectOutputStream.close();
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                }
            }
            return false;
        } catch (IOException e) {
            if (e instanceof SocketException)
                Announcement.showAnnouncement("", Alert.AlertType.ERROR,
                        "Cannot send message",
                        ipAddr + " is unreachable!");
            else
                e.printStackTrace();
            return false;
        }
    }
}
