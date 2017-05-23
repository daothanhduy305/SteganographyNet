package ebolo.net.listeners;

import ebolo.net.connections.OutgoingConnection;
import ebolo.net.data.Message;
import ebolo.ui.controller.SendWindowController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ebolo on 18/05/2017.
 */
public class OutgoingListener {
    private static OutgoingListener ourInstance;
    private ExecutorService executorService;

    public static OutgoingListener getInstance() {
        if (ourInstance == null)
            ourInstance = new OutgoingListener();
        return ourInstance;
    }

    private OutgoingListener() {
        executorService = Executors.newFixedThreadPool(5);
    }

    public void send(Message message, String ipAddr) throws Exception {
        Future<Boolean> result = executorService.submit(new OutgoingConnection(message, ipAddr));
        if (result.get()) {
            SendWindowController.getInstance().close();
        }
    }

    public void stopListening() {
        if (executorService != null)
            executorService.shutdown();
    }
}
