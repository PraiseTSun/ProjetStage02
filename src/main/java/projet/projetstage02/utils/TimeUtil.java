package projet.projetstage02.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtil {
    public static long currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }
}
