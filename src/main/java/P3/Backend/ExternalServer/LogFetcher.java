package P3.Backend.ExternalServer;

import com.github.dockerjava.api.DockerClient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class LogFetcher {
    private final Map<String,Long> last = new HashMap<>();

    public JSONObject fetch(DockerClient dockerClient, Integer interval, String id) throws InterruptedException {
        FilteredLogCallback cb = new FilteredLogCallback();
        long nowMillis = System.currentTimeMillis();
        long sinceMillis = nowMillis - (interval * 1000L);
        int sinceSeconds = (int) (sinceMillis / 1000L);

        dockerClient.logContainerCmd(id)
                .withStdOut(true)
                .withStdErr(true)
                .withSince(sinceSeconds) 
                .exec(cb)
                .awaitCompletion();

        last.put(id, System.currentTimeMillis());
        JSONObject out = cb.get();

        return out;
    }
}