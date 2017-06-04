package ebolo.net.listeners;

import com.sun.istack.internal.Nullable;
import ebolo.Main;
import ebolo.ui.utils.Announcement;
import ebolo.utils.Configurations;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ebolo on 6/4/17.
 */
public class ButlerListener {
    private static ButlerListener ourInstance;
    private Thread butlerThread;
    private ServerSocket butlerSocket;

    public static ButlerListener getInstance() {
        if (ourInstance == null)
            ourInstance = new ButlerListener();
        return ourInstance;
    }

    private ButlerListener() {
        butlerThread = new Thread(new ButlerJob());
    }

    public void prepare() {
        butlerThread.start();
    }

    public void stopListening() {
        try {
            butlerThread.interrupt();
            butlerSocket.close();
            IncomingListener.getInstance().stopListening();
            OutgoingListener.getInstance().stopListening();
            new Thread(() -> {
                try {
                    synchronized (Configurations.getInstance()) {
                        Configurations.getInstance().save();
                    }
                } catch (IOException e) {
                    synchronized (System.out) {
                        System.out.println("Can't save configurations");
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ButlerJob implements Runnable {

        @Override
        public void run() {
            ServerSocket incomingSocket = getAvailablePort(Configurations.getInstance()
                    .getIncomingPort());
            butlerSocket = getAvailablePort(Configurations.getInstance()
                    .getButlerPort());
            if (incomingSocket != null && butlerSocket != null) {
                Configurations.getInstance().setIncomingPort(incomingSocket.getLocalPort());
                Configurations.getInstance().setButlerPort(butlerSocket.getLocalPort());
                IncomingListener.getInstance().startListening(incomingSocket);
                try {
                    while (true) {
                        Socket peer = butlerSocket.accept();
                        System.out.println("Have a guest coming. Showing the way...");
                        ObjectOutputStream stream = new ObjectOutputStream(
                                new BufferedOutputStream(
                                        peer.getOutputStream()
                                )
                        );
                        stream.flush();
                        stream.writeUTF("Steganography " + incomingSocket.getLocalPort());
                        stream.close();
                        peer.close();
                    }
                } catch (IOException e) {
                    synchronized (System.out) {
                        System.out.println("Cleaning house...");
                    }
                }
            } else {
                System.out.println("Cannot find any port to bind ");
                Platform.runLater(() -> {
                    Announcement.showAnnouncement(
                            "",
                            Alert.AlertType.ERROR,
                            "Cannot Start Application",
                            "Cannot bind socket to port "
                                    + Configurations.getInstance().getIncomingPort()
                    );
                    Main.getMainStage().close();
                });
            }
        }

        private ServerSocket getAvailablePort(@Nullable Integer initPort) {
            ServerSocket serverSocket = null;
            synchronized (System.out) {
                if (initPort != null) {
                    try {
                        serverSocket = new ServerSocket(initPort);
                        serverSocket.setReuseAddress(true);
                        return serverSocket;
                    } catch (IOException e) {
                        System.out.print("Default port is not available. Start Scanning...");
                    }
                }

                for (int tryingPort = 49154; tryingPort < 65535; tryingPort++) {
                    try {
                        serverSocket = new ServerSocket(tryingPort);
                        serverSocket.setReuseAddress(true);
                        System.out.println("\nGot one port free at " + tryingPort);
                        return serverSocket;
                    } catch (IOException e1) {
                        System.out.print('.');
                    }
                }
            }
            return serverSocket;
        }
    }

}
