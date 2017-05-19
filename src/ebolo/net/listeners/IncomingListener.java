package ebolo.net.listeners;

import ebolo.net.connections.IncomingConnection;
import ebolo.utils.Configurations;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ebolo on 18/05/2017.
 */
public class IncomingListener {
    private static IncomingListener ourInstance;
    private Thread listeningThread;

    public static IncomingListener getInstance() {
        if (ourInstance == null)
            ourInstance = new IncomingListener();
        return ourInstance;
    }

    private IncomingListener() {
    }

    public void startListening() {
        listeningThread = new Thread(this::startServer);
        listeningThread.start();
    }


    public void stopListening() {
        listeningThread.interrupt();
    }

    private void startServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Configurations.getPORT());
            while (true) {
                Socket peer = serverSocket.accept();
                new Thread(new IncomingConnection(peer)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
