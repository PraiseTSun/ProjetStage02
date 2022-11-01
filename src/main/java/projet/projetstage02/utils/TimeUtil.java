package projet.projetstage02.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class TimeUtil {
    public static final long MILLI_SECOND_DAY = 864000000;

    public static long currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    public static int getNextYear() {
        return LocalDateTime.now().getYear() + 1;
    }

    public static boolean isRightSession(String session, int year) {
        if (session == null) {
            return false;
        }
        Pattern regexp = Pattern.compile("^Hiver (\\d{4})$");
        return (regexp.matcher(session).matches())
                && ("Hiver " + year).equals(session);
    }
}
