package ebolo.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ebolo on 15/05/2017.
 */
public class ImageUtils {
    public static Image imageIconToFX(ImageIcon imageIcon) {
        BufferedImage bufferedImage = new BufferedImage(
                imageIcon.getIconWidth(),
                imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.createGraphics();
        // paint the Icon to the BufferedImage.
        imageIcon.paintIcon(null, graphics, 0,0);
        graphics.dispose();
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}