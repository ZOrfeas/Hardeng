package Hardeng.Rest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This is probably the best way to group some globals to
 * allow for easier future changes, should the need arise.
 */
public class Utilities {
    public static final String BASEURL = "/evcharge/api";
    public static final String[] FORMATS = {"application/json", "text/csv"};
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static Timestamp timestampFromString(String orgString, SimpleDateFormat timestampFormat) {
        try {
            return new Timestamp(timestampFormat.parse(orgString).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
