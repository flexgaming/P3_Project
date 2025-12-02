package P3.Backend;

import java.util.HashMap;
import java.util.Map;

public final class SeverityCalculator {
    /**
     * Framework method for assessing severity of one or more diagnostics.
     *
     * Current behavior: each error entry is assigned a `severity` field set to
     * "UNKNOWN". This provides a consistent structure for later insertion of
     * actual assessment logic without requiring callers to change.
     *
     * @param errors map containing diagnostics errors (may contain many entries)
     * @return a new map where each key maps to a Map with keys `data` and `severity`.
     */
    public Map<String, Object> assessSeverity(Map<String, Object> errors) {
        if (errors == null) return Map.of(); // Return empty map if input is null
        //Loop for changing each error entry to include severity
        Map<String, Object> results = new HashMap<>();

        for (String key : errors.keySet()) {
            Object entry = errors.get(key);
            if (entry instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> assessedEntry = assessSingleSeverity((Map<String, Object>) entry);
                results.put(key, assessedEntry);
            }
        }

        return results;
    }

    public Map<String, Object> assessSingleSeverity(Map<String, Object> errorEntry) {
        //////////////////////////////////////////////////
        //Add severity assessment logic here in future////
        //////////////////////////////////////////////////
        String severity = "UNKNOWN";

        errorEntry.put("severity", severity);
        return errorEntry;
    }
}