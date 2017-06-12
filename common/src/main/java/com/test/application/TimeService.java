package com.test.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for performing operation on times, converting to UTC etc.
 *
 * Created by rahulpawar.
 */
public class TimeService {

    /**
     * API to give current timestamp in UTC time zone.
     *
     * @return current timestamp in UTC.
     */
    public static Long getCurrentUTCTime() throws ParseException{
        final DateFormat utcDateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        utcDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String currentDateInUtc = utcDateFormatter.format(new Date());
        return utcDateFormatter.parse(currentDateInUtc).getTime();
    }
}
