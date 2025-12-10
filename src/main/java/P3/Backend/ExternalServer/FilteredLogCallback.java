package P3.Backend.ExternalServer;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class FilteredLogCallback extends ResultCallback.Adapter<Frame> {
    private final JSONArray errors = new JSONArray();
    private final JSONArray warns = new JSONArray();
    

    @Override
    public void onNext(Frame frame) {
        
        String payload = new String(frame.getPayload(), StandardCharsets.UTF_8);
        if (payload.isEmpty()) return;

        
        String[] lines = payload.split("\\r?\\n");
        for (String line : lines) {
            if (line == null) continue;
            String trimmed = line.trim(); 
            if (trimmed.isEmpty()) continue;

            String up = trimmed.toUpperCase();
            if (up.contains("ERROR")) {
                errors.put(trimmed);
            } else if (up.contains("WARN")) {
                warns.put(trimmed);
            } 
        }
    }

    
    public JSONObject get() {
        JSONObject result = new JSONObject();
        result.put("Error", errors);
        result.put("Warn", warns);
        
        return result;
    }
}