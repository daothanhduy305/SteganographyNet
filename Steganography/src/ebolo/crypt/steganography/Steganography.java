package ebolo.crypt.steganography;

import ebolo.crypt.steganography.utils.SteganographyUtils;
import ebolo.utils.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.swing.*;

/**
 * Created by ebolo on 17/05/2017.
 */
public class Steganography {

    public static ImageIcon encrypt(String message, Image originalImage) {
        Image result = encryptInternal(message, originalImage);
        if (result != null)
            return new ImageIcon(SwingFXUtils.fromFXImage(result, null));
        return null;
    }

    public static Image encrypt(Image originalImage, String message) {
        return encryptInternal(message, originalImage);
    }

    private static WritableImage encryptInternal(String message, Image originalImage) {
        boolean[] messageBits = SteganographyUtils.stringToBits(message);
        int imageWidth = (int) originalImage.getWidth(),
                imageHeight = (int) originalImage.getHeight();

        if (message.length() + 33 > (imageWidth / 4) * (imageHeight / 4) * 3) {
            // This input is longer than the limit size of the image
            // TODO: find a way to throw out error
            return null;
        } else {
            WritableImage encryptedImage = new WritableImage(
                    originalImage.getPixelReader(),
                    imageWidth, imageHeight
            );

            imageEncode(messageBits, encryptedImage, 0);

            boolean[] lengthBits = SteganographyUtils.intToBits(message.length());

            imageEncode(lengthBits, encryptedImage, imageWidth * imageHeight - 11);

            return encryptedImage;
        }
    }

    private static void imageEncode(boolean[] messageBits, WritableImage encryptedImage, int start) {
        int pixelCounter = start;
        for (int messageBitCounter = 0;
             messageBitCounter < messageBits.length;
             messageBitCounter += 3)
        {
            int red = SteganographyUtils.encode(
                    (int) (encryptedImage.getPixelReader().getColor(
                            pixelCounter % (int) encryptedImage.getWidth(),
                            pixelCounter / (int) encryptedImage.getWidth()
                    ).getRed() * 255),
                    messageBits[messageBitCounter]
            );

            int green = (int) (encryptedImage.getPixelReader().getColor(
                    pixelCounter % (int) encryptedImage.getWidth(),
                    pixelCounter / (int) encryptedImage.getWidth()
            ).getGreen() * 255);

            if (messageBitCounter + 1 < messageBits.length)
                green = SteganographyUtils.encode(green, messageBits[messageBitCounter + 1]);

            int blue = (int) (encryptedImage.getPixelReader().getColor(
                    pixelCounter % (int) encryptedImage.getWidth(),
                    pixelCounter / (int) encryptedImage.getWidth()
            ).getBlue() * 255);

            if (messageBitCounter + 2 < messageBits.length)
                blue = SteganographyUtils.encode(blue, messageBits[messageBitCounter + 2]);

            encryptedImage.getPixelWriter().setColor(
                    pixelCounter % (int) encryptedImage.getWidth(),
                    pixelCounter / (int) encryptedImage.getWidth(),
                    Color.rgb(red, green, blue)
            );

            pixelCounter++;
        }
    }

    public static String decrypt(ImageIcon inputImage) {
        Image encryptedImage = ImageUtils.imageIconToFX(inputImage);
        int imageWidth = (int) encryptedImage.getWidth(),
                imageHeight = (int) encryptedImage.getHeight();

        int length = SteganographyUtils.bitsToInt(
                readFromImage(encryptedImage,
                        imageHeight * imageWidth - 11,
                        32),
                32
        );

        return SteganographyUtils.bitsToString(
                readFromImage(encryptedImage, 0, length * 16)
        );
    }

    public static String decrypt(Image image) {
        return decrypt(new ImageIcon(SwingFXUtils.fromFXImage(image, null)));
    }

    private static boolean[] readFromImage(Image image, int start, int length) {
        boolean[] result = new boolean[length];

        int pixelCounter = start;
        for (int i = 0; i < length; i += 3) {
            result[i] = SteganographyUtils.intToBits(
                    (int) (image.getPixelReader().getColor(
                            (int) (pixelCounter % image.getWidth()),
                            (int) (pixelCounter / image.getWidth())).getRed() * 255))[31];

            if (i + 1 < length)
                result[i + 1] = SteganographyUtils.intToBits(
                        (int) (image.getPixelReader().getColor(
                                (int) (pixelCounter % image.getWidth()),
                                (int) (pixelCounter / image.getWidth())).getGreen() * 255))[31];

            if (i + 2 < length)
                result[i + 2] = SteganographyUtils.intToBits(
                        (int) (image.getPixelReader().getColor(
                                (int) (pixelCounter % image.getWidth()),
                                (int) (pixelCounter / image.getWidth())).getBlue() * 255))[31];

            pixelCounter++;
        }

        return result;
    }
}
