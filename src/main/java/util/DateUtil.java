package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static boolean matchesInPeriod(LocalDate target, LocalDate start, LocalDate end){
       if(target == null) return false;
       if(start == null && end ==null)
           return true;
       if (start == null)
           return !target.isAfter(end);
       if (end == null)
           return !target.isBefore(start);
       return !target.isBefore(start) && !target.isAfter(end);
    }

    public static long getDDay(LocalDate target){
        if(target == null) return 0;
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(today, target);
    }

    public static String getDDayText(LocalDate target) {
        if (target == null) return "";

        long diff = getDDay(target);
        if (diff == 0) return "D-day";
        if (diff > 0) return "D-" + diff;
        return "";
    }
}
