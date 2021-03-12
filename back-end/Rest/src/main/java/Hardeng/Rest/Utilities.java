package Hardeng.Rest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

// import org.springframework.beans.factory.annotation.Value;

/** Utility methods, constants and classes for ease of use access and editting */
public class Utilities {
    public static final String BASEURL = "/evcharge/api";
    public static final String[] FORMATS = {"application/json", "text/csv"};
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** A very bad practice hardcoded INTO code security constant declaration group that should have been changed */
    public static class SecurityConstants {
        public static final long validity_time = 5*60*60;
        public static final long a_long_time = 60*60*24*365;
        // @Value("${jwt.secret}")
        public static final String secret = "developmentjwtkey";
        // @Value("${jwt.header}")
        public static final String header_name = "X-OBSERVATORY-AUTH";
        // @Value("${master.username}")
        public static final String masterUsername = "master-of-puppets";
        // @Value("${master.password}")
        public static final String masterPassword = "for-whom-the-bell-tolls";

    }

    /** Utility method parsing string into timestamp
     * @param orgString string to parse
     * @param timestampFormat format to fit it too
     * @return timestamp object if string format is correct
     */
    public static Timestamp timestampFromString(String orgString, SimpleDateFormat timestampFormat) {
        try {
            return new Timestamp(timestampFormat.parse(orgString).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** Any class that wants to be serialized into CSV needs to implement this */
    public static interface CsvObject {
        /** This will be a list of simple csv-able objects to serialize into response body
         * @return the list of beans to output
         */
        List<Object> getList();
    }


}
