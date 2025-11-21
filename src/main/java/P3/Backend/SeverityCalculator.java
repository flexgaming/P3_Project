package P3.Backend;

import java.util.HashMap;
import java.util.Map;

public final class SeverityCalculator {

    /**
     * Framework method for assessing severity of one or more diagnostics.
     *
     * Current behavior: for each entry in `errorData` returns a wrapper map
     * containing the original value under `data` and a `severity` field set to
     * "UNKNOWN". This provides a consistent structure for later insertion of
     * actual assessment logic without requiring callers to change.
     *
     * @param errorData map containing diagnostics (may contain many entries)
     * @return a new map where each key maps to a Map with keys `data` and `severity`.
     */
    public Map<String, Object> assessSeverity(Map<String, Object> errorData) {
        if (errorData == null) return Map.of();

        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : errorData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("data", value);
            wrapper.put("severity", "UNKNOWN");

            result.put(key, wrapper);
        }

        //return result;
        return errorData;
    }
}
