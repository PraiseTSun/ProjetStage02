package projet.projetstage02.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtil {
    public static final long MILLI_SECOND_DAY = 864000000;

    public static long currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    public static int getNextYear() {
        return LocalDateTime.now().getYear() + 1;
    }
}
