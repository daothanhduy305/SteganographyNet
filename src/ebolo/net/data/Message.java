package ebolo.net.data;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ebolo on 18/05/2017.
 */
public class Message {
    private String date;
    private ImageIcon encryptedImage;

    public Message(ImageIcon encryptedImage) {
        this.encryptedImage = encryptedImage;
        date = (new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss")).format(new Date());
    }

    public String getDate() {
        return date;
    }

    public ImageIcon getEncryptedImage() {
        return encryptedImage;
    }
}
