package ebolo.crypt.utils;

/**
 * Created by ebolo on 17/05/2017.
 */
public class SteganographyUtils {
    public static boolean[] charToBits(char c) {
        boolean[] result = new boolean[16];
        final int mask = 0x8000;
        for (int i = 0; i < 16; i++) {
            result[i] = (((int) c) & mask) != 0;
            c <<= 1;
        }
        return result;
    }

    public static boolean[] stringToBits(String string) {
        boolean[] result = new boolean[16 * string.length()];
        int counter = 0;
        for (char c : string.toCharArray()) {
            boolean[] charBits = charToBits(c);
            for (int i = 0; i < 16; i++) {
                result[counter++] = charBits[i];
            }
        }
        return result;
    }

    public static int encode(int color, boolean bit) {
        if (bit)
            return color | 1;
        else
            return color & ~1;
    }

    public static boolean[] intToBits(int input) {
        boolean[] result = new boolean[32];
        final int mask = 0x80000000;
        for (int i = 0; i < 32; i++) {
            result[i] = (input & mask) != 0;
            input <<= 1;
        }
        return result;
    }

    public static int bitsToInt(boolean[] bits, int size) {
        int result = 0;
        for (int i = size - 1; i >= 0; i--) {
            if (bits[i])
                result += (int) Math.pow(2.0, (double) (size - 1 - i));
        }
        return result;
    }

    public static char bitsToChar(boolean[] bits) {
        int c = bitsToInt(bits, 16);
        return (char) c;
    }

    public static String bitsToString(boolean[] bits) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < bits.length; i += 16) {
            boolean[] charBits = new boolean[16];
            for (int j = 0; j < 16; j++) {
                charBits[j] = bits[i + j];
            }
            stringBuilder.append(bitsToChar(charBits));
        }
        return stringBuilder.toString();
    }

    public static void printBits(boolean[] bits) {
        for (int i = 0; i < bits.length; i++) {
            if (i%4 == 0 && i > 0)
                System.out.print(' ');
            System.out.print(bits[i]? '1' : '0');
        }
        System.out.println();
    }
}
