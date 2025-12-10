package P3.Backend.ExternalServer;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class FilteredLogCallback extends ResultCallback.Adapter<Frame> {
    private final JSONArray errors = new JSONArray();
    private final JSONArray warns = new JSONArray();
    // private final JSONArray infos = new JSONArray();

    @Override
    public void onNext(Frame frame) {
        // Gets the raw bytes from the frame payload.
        String payload = new String(frame.getPayload(), StandardCharsets.UTF_8);
        if (payload.isEmpty()) return;

        // Split payload into separate lines to handle batched frames
        String[] lines = payload.split("\\r?\\n");
        for (String line : lines) {
            if (line == null) continue;
            String trimmed = line.trim(); // Trim whitespace and skip empty lines
            if (trimmed.isEmpty()) continue;

            String up = trimmed.toUpperCase();
            if (up.contains("ERROR")) {
                errors.put(trimmed);
            } else if (up.contains("WARN")) {
                warns.put(trimmed);
            } /* else if (up.contains("INFO")) {
                infos.put(trimmed);
            } */
        }
    }

    // Get all of the data put into an JSONObject.
    public JSONObject get() {
        JSONObject result = new JSONObject();
        result.put("Error", errors);
        result.put("Warn", warns);
        //result.put("Info", infos);
        return result;
    }
}