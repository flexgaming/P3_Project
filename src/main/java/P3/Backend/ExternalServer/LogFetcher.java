package P3.Backend.ExternalServer;

import com.github.dockerjava.api.DockerClient;
import java.util.HashMap;
import java.util.Map;

public class LogFetcher {
    private final Map<String,Long> last = new HashMap<>();

    public String fetch(DockerClient dc, String id) throws InterruptedException {
        long since = last.getOrDefault(id, System.currentTimeMillis() - 60000);
        FilteredLogCallback cb = new FilteredLogCallback();

        dc.logContainerCmd(id)
          .withStdOut(true)
          .withStdErr(true)
          .withSince((int)(since/1000))
          .exec(cb)
          .awaitCompletion();

        last.put(id, System.currentTimeMillis());
        return cb.get();
    }

    // TODO Add support for ContainerClass, needs last log copied timestamp.
}