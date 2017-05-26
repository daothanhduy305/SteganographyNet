package ebolo.utils;

import ebolo.Main;

import java.io.*;

/**
 * Created by ebolo on 26/05/2017.
 */
public class Configurations implements Serializable {
    private static Configurations ourInstance;
    private static final String jarFilePath =
            Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    private static final String workingDir =
            (new StringBuilder(jarFilePath))
                    .delete(
                            jarFilePath.lastIndexOf(File.separatorChar) + 1,
                            jarFilePath.length())
                    .toString();
    private static final String configPath =
            new StringBuilder(workingDir).append("app.cfg").toString();
    private static final int PORT = 3005;
    private String lastUsedPath;

    public static Configurations getInstance() {
        File configFile = new File(configPath);
        if (ourInstance == null) {
            try {
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

    public int getPORT() {
        return PORT;
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
