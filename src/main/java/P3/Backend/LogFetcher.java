package P3.Backend;

import com.github.dockerjava.api.DockerClient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
/** This function is used in order to get all of the "warn" and "error" in the log.
 * 
 * @param dockerClient Is used in order to get a connection to the docker server.
 * @param id Is used in order to find the logs from the specific container.
 * @param interval Is used in order to see what have happened since last check ( This have to be in miliseconds: 1s = 1000 ).
 * @return 
 * @throws InterruptedException Is used if the thread is interrupted while waiting for log retrieval to complete.
 */

public class LogFetcher {
    private final Map<String,Long> last = new HashMap<>();

    public JSONObject fetch(DockerClient dc, Integer interval, String id) throws InterruptedException {
      FilteredLogCallback cb = new FilteredLogCallback();
      long nowMillis = System.currentTimeMillis();
      long sinceMillis = nowMillis - (interval * 1000L);
      int sinceSeconds = (int) (sinceMillis / 1000L);

        dc.logContainerCmd(id)
          .withStdOut(true)
          .withStdErr(true)
          .withSince(sinceSeconds) // How far back it looks for logs
          .exec(cb)
          .awaitCompletion();

        last.put(id, System.currentTimeMillis());
        JSONObject out = cb.get();

        return out;
    }
}