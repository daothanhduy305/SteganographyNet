package ebolo.crypt.steganography.utils;

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

    public static boolean[] byteToBits(byte input) {
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; i++) {
            result[i] = ((int) input & 0x80) != 0;
            input <<= 1;
        }
        return result;
    }

    public static boolean[] bytesToBits(byte[] input) {
        boolean[] result = new boolean[8 * input.length];
        int counter = 0;
        for (byte b : input) {
            boolean[] bits = byteToBits(b);
            for (int i = 0; i < 8; i++) {
                result[counter++] = bits[i];
            }
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

    public static byte bitsToByte(boolean[] bits) {
        byte b = (byte) 0;
        for (int i = 7; i >= 0; i--) {
            if (bits[i]) {
                b |= (1 << (7 - i));
            }
        }
        return b;
    }

    public static byte[] bitsToBytes(boolean[] bits) {
        byte[] result = new byte[bits.length / 8];
        for (int i = 0; i < bits.length; i += 8) {
            boolean[] temp = new boolean[8];
            for (int j = 0; j < 8; j++) {
                temp[j] = bits[i + j];
            }
            result[i / 8] = bitsToByte(temp);
        }
        return result;
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