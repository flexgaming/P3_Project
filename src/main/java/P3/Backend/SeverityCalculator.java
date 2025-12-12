package P3.Backend;

import java.util.HashMap;
import java.util.Map;

public final class SeverityCalculator {
    public Map<String, Object> assessSeverity(Map<String, Object> errors) {
        if (errors == null) return Map.of(); 
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
        String severity = "UNKNOWN";

        errorEntry.put("severity", severity);
        return errorEntry;
    }
}