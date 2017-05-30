package ebolo.net.listeners;

import ebolo.Main;
import ebolo.net.connections.IncomingConnection;
import ebolo.ui.utils.Announcement;
import ebolo.utils.Configurations;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.BindException;
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
        if (serverSocket != null)
            serverSocket.close();
        listeningThread.interrupt();
    }

    private void startServer() {
        try {
            synchronized (Configurations.getInstance()) {
                try {
                    serverSocket = new ServerSocket(Configurations.getInstance().getPORT());
                } catch (BindException e) {
                    System.out.println(
                            "Cannot bind socket to port " + Configurations.getInstance().getPORT()
                    );
                    Platform.runLater(() -> {
                        Announcement.showAnnouncement(
                                "",
                                Alert.AlertType.ERROR,
                                "Cannot Start Application",
                                "Cannot bind socket to port "
                                        + Configurations.getInstance().getPORT()
                        );
                        Main.getMainStage().close();
                    });
                }
            }
            if (serverSocket != null) {
                System.out.println("Start listening");
                while (true) {
                    Socket peer = serverSocket.accept();
                    new Thread(new IncomingConnection(peer)).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Stopped listening");
        }
    }
}
