package P3.Backend;

import com.github.dockerjava.api.DockerClient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class LogFetcher {
    private final Map<String,Long> last = new HashMap<>();

    public JSONObject fetch(DockerClient dc, Integer interval, String id) throws InterruptedException {
      FilteredLogCallback cb = new FilteredLogCallback();
      long nowMillis = System.currentTimeMillis();
      long sinceMillis = nowMillis - (interval * 1000L);
      int sinceSeconds = (int) (sinceMillis / 1000L); // Docker expects UNIX epoch seconds

        dc.logContainerCmd(id)
          .withStdOut(true)
          .withStdErr(true)
          .withSince(sinceSeconds)
          .exec(cb)
          .awaitCompletion();

        last.put(id, System.currentTimeMillis());
        JSONObject out = cb.get();
        try {
          int e = out.has("Error") ? out.getJSONArray("Error").length() : 0;
          int w = out.has("Warn") ? out.getJSONArray("Warn").length() : 0;
          int i = out.has("Info") ? out.getJSONArray("Info").length() : 0;
          System.out.println("\n\n[LogFetcher] id=" + id + " counts -> Error=" + e + ", Warn=" + w + ", Info=" + i + "\n\n");
        } catch (Exception ignore) {}
        return out;
    }
}