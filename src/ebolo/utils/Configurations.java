package ebolo.utils;

import ebolo.Main;

import java.io.*;

/**
 * Created by ebolo on 26/05/2017.
 */
public class Configurations implements Serializable {
    private static Configurations ourInstance;
    private static final String jarFilePath =
            Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getFile()
                    .replace('/', File.separatorChar);
    private static final String workingDir =
            (new StringBuilder(jarFilePath))
                    .delete(
                            jarFilePath.lastIndexOf(File.separatorChar) + 1,
                            jarFilePath.length())
                    .toString();
    private static final String configPath =
            new StringBuilder(workingDir)
                    .append("app.cfg")
                    .toString();
    private int incomingPort = 49153;
    private int butlerPort = 49152;
    private String lastUsedPath;

    public static Configurations getInstance() {
        if (ourInstance == null) {
            try {
                File configFile = new File(configPath);
                ObjectInputStream inputStream = new ObjectInputStream(
                        new BufferedInputStream(
                                new FileInputStream(configFile)
                        )
                );
                ourInstance = (Configurations) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                ourInstance = new Configurations();
            }
        }
        return ourInstance;
    }

    private Configurations() {
    }

    public int getIncomingPort() {
        return incomingPort;
    }

    public void setIncomingPort(int incomingPort) {
         this.incomingPort = incomingPort;
    }

    public int getButlerPort() {
        return butlerPort;
    }

    public void setButlerPort(int butlerPort) {
        this.butlerPort = butlerPort;
    }

    public void setLastUsedPath(String lastUsedPath) {
        this.lastUsedPath = lastUsedPath;
    }

    public String getLastUsedPath() {
        return lastUsedPath == null? workingDir : lastUsedPath;
    }

    public void save() throws IOException {
        File configFile = new File(configPath);
        boolean isOK = configFile.exists();
        if (!configFile.exists())
            isOK = configFile.createNewFile();
        if (isOK) {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(configFile)
                    )
            );
            outputStream.writeObject(this);
            outputStream.flush();
            outputStream.close();
        }
    }
}
