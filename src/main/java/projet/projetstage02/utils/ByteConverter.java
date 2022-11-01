package projet.projetstage02.utils;

import java.util.Arrays;

public class ByteConverter {
    public static String byteToString(byte[] byteArray) {
        return Arrays.toString(byteArray).replaceAll("\\s+", "");
    }

    public static byte[] stringToBytes(String string) {
        if (string == null || string.equals("null"))
            return new byte[0];
        string = string.replaceAll("\\[*]*", "");
        if (string.length() == 0) {
            return new byte[0];
        }
        String[] split = string.split(",");
        byte[] bytes = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bytes[i] = Byte.parseByte(split[i]);
        }
        return bytes;
    }
}
