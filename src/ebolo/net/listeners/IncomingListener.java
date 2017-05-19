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
    ServerSocket serverSocket;

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


    public void stopListening() throws IOException {
        serverSocket.close();
        listeningThread.interrupt();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(Configurations.getPORT());
            System.out.println("Start listening");
            while (true) {
                Socket peer = serverSocket.accept();
                new Thread(new IncomingConnection(peer)).start();
            }
        } catch (IOException e) {
            System.out.println("Stopped listening");
        }
    }
}
