package projet.projetstage02.utils;

import java.util.Arrays;

public class ByteConverter {
    public static String byteToString(byte[] byteArray) {
        return Arrays.toString(byteArray).replaceAll("\\s+", "");
    }
}
