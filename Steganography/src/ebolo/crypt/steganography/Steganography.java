package ebolo.crypt.steganography;

import ebolo.crypt.steganography.utils.SteganographyUtils;
import ebolo.utils.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ebolo on 17/05/2017.
 */
public class Steganography {

    public static ImageIcon encryptM(String message, Image originalImage) throws IOException {
        Image result = encryptInternal(message, originalImage, false);
        if (result != null)
            return new ImageIcon(SwingFXUtils.fromFXImage(result, null));
        return null;
    }

    public static ImageIcon encryptF(Path filePath, Image originalImage) throws IOException {
        Image result = encryptInternal(filePath.toString(), originalImage, true);
        if (result != null)
            return new ImageIcon(SwingFXUtils.fromFXImage(result, null));
        return null;
    }


    private static WritableImage encryptInternal(
            String message, Image originalImage, boolean isFile) throws IOException {

        int imageWidth = (int) originalImage.getWidth(),
                imageHeight = (int) originalImage.getHeight();
        WritableImage encryptedImage = new WritableImage(
                originalImage.getPixelReader(),
                imageWidth, imageHeight
        );

        if (isFile) {
            File file = new File(message);
            if (file.exists()) {
                StringBuilder extensionBuilder = new StringBuilder("");
                for (int i = message.length() - 1; i >= 0; i--) {
                    if (message.charAt(i) == '.')
                        break;
                    extensionBuilder.append(message.charAt(i));
                }
                boolean[] extensionBits = SteganographyUtils.stringToBits(extensionBuilder.reverse().toString());
                boolean[] extensionLengthBits = SteganographyUtils.intToBits(extensionBuilder.toString().length());
                boolean[] fileBits = SteganographyUtils.bytesToBits(Files.readAllBytes(file.toPath()));
                boolean[] fileLengthBits = SteganographyUtils.intToBits(fileBits.length / 8);
                if (extensionBits.length + fileBits.length + 64
                        <= imageHeight * imageWidth * 3) {
                    imageEncode(extensionBits, encryptedImage, 0);
                    imageEncode(fileBits, encryptedImage, extensionBits.length / 3 + 1);
                    imageEncode(extensionLengthBits, encryptedImage, imageHeight * imageWidth - 11);
                    imageEncode(fileLengthBits, encryptedImage, imageHeight * imageWidth - 22);
                    return encryptedImage;
                }
                return null;
            }
            return null;
        } else {
            boolean[] messageBits = SteganographyUtils.stringToBits(message);
            if (message.length() + 33 > (imageWidth / 4) * (imageHeight / 4) * 3) {
                // This input is longer than the limit size of the image
                // TODO: find a way to throw out error
                return null;
            } else {
                imageEncode(messageBits, encryptedImage, 0);
                boolean[] lengthBits = SteganographyUtils.intToBits(message.length());
                imageEncode(lengthBits, encryptedImage, imageWidth * imageHeight - 11);
                return encryptedImage;
            }
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

    public static String decryptM(ImageIcon inputImage) {
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

    public static String decryptM(Image image) {
        return decryptM(new ImageIcon(SwingFXUtils.fromFXImage(image, null)));
    }

    public static File decryptF(ImageIcon inputImage, String fileName) throws IOException {
        Image encryptedImage = ImageUtils.imageIconToFX(inputImage);
        int imageWidth = (int) encryptedImage.getWidth(),
                imageHeight = (int) encryptedImage.getHeight();

        int extensionLength = SteganographyUtils.bitsToInt(
                readFromImage(
                        encryptedImage,
                        imageHeight * imageWidth - 11,
                        32),
                32
        );
        int fileLength = SteganographyUtils.bitsToInt(
                readFromImage(
                        encryptedImage,
                        imageHeight * imageWidth - 22,
                        32),
                32
        );
        byte[] fileBytes = SteganographyUtils.bitsToBytes(
                readFromImage(
                        encryptedImage,
                        extensionLength * 16 / 3 + 1,
                        fileLength * 8)
        );
        String extension = SteganographyUtils.bitsToString(
                readFromImage(
                        encryptedImage,
                        0,
                        extensionLength * 16)
        );

        //Write file
        File result = new File(fileName + '.' + extension);
        boolean isOK = true;
        if (!result.exists())
            isOK = result.createNewFile();

        if (isOK) {
            FileOutputStream fileOutputStream = new FileOutputStream(result);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(fileBytes);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return result;
        }
        return null;
    }

    public static File decryptF(Image image, String fileName) throws IOException {
        return decryptF(new ImageIcon(SwingFXUtils.fromFXImage(image, null)), fileName);
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
