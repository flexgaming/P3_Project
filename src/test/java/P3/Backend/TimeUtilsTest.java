package P3.Backend;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void splitUtcReturnsSameDateTimeWithoutOffset() {
        String ts = "2025-11-12T07:32:59.117+00:00";
        String[] parts = TimeUtils.splitIsoToLocalDateAndTime(ts, ZoneId.of("UTC"));
        assertEquals(2, parts.length);
        assertEquals("2025-11-12", parts[0]);
        assertEquals("07:32:59.117", parts[1]);
    }

    @Test
    void splitToLosAngelesConvertsToPreviousDay() {
        String ts = "2025-11-12T07:32:59.117+00:00";
        // America/Los_Angeles is UTC-8 on this date (standard time) so the local time should be previous day 23:32:59.117
        String[] parts = TimeUtils.splitIsoToLocalDateAndTime(ts, ZoneId.of("America/Los_Angeles"));
        assertEquals(2, parts.length);
        assertEquals("2025-11-11", parts[0]);
        assertEquals("23:32:59.117", parts[1]);
    }
}
