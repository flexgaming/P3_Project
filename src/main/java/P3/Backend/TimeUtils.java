package P3.Backend;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Small utility for parsing ISO offset timestamps and returning local date and time strings.
 */
public final class TimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private TimeUtils() { }

    /**
     * Parse an ISO_OFFSET_DATE_TIME (e.g. 2025-11-12T07:32:59.117+00:00), convert to the system default zone,
     * format using a date'T'time pattern and split on the literal 'T'.
     * @param isoTimestamp the input timestamp
     * @return String[0] = date (yyyy-MM-dd), String[1] = time (HH:mm:ss.SSS)
     */
    public static String[] splitIsoToLocalDateAndTime(String isoTimestamp) {
        return splitIsoToLocalDateAndTime(isoTimestamp, ZoneId.systemDefault());
    }

    /**
     * Same as above but allows specifying the target zone to convert to (useful for deterministic tests).
     * @param isoTimestamp the input timestamp
     * @param zone target zone (e.g. ZoneId.systemDefault())
     * @return String[0] = date (yyyy-MM-dd), String[1] = time (HH:mm:ss.SSS)
     */
    public static String[] splitIsoToLocalDateAndTime(String isoTimestamp, ZoneId zone) {
        if (isoTimestamp == null) throw new IllegalArgumentException("isoTimestamp must not be null");
        OffsetDateTime odt = OffsetDateTime.parse(isoTimestamp);
        ZonedDateTime zdt = odt.toInstant().atZone(zone);
        LocalDateTime ldt = zdt.toLocalDateTime();

        String formatted = ldt.format(FORMATTER);
        // split by the literal 'T' (limit=2 to avoid surprises)
        return formatted.split("T", 2);
    }
}
