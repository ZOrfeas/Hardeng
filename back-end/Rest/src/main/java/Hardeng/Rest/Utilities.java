package Hardeng.Rest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

/**
 * This is probably the best way to group some globals to
 * allow for easier future changes, should the need arise.
 */
public class Utilities {
    public static final String BASEURL = "/evcharge/api";
    public static final String[] FORMATS = {"application/json", "text/csv"};
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static class SecurityConstants {
        @Value("${jwt.secret}")
        public static String secret;
        public static final long validity_time = 5*60*60;
        @Value("${jwt.header-name}")
        public static String header_name;
    }

    public static Timestamp timestampFromString(String orgString, SimpleDateFormat timestampFormat) {
        try {
            return new Timestamp(timestampFormat.parse(orgString).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static interface CsvObject {
        List<Object> getList();
    }


}
