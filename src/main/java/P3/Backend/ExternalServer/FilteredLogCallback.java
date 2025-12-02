package P3.Backend.ExternalServer;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;

public class FilteredLogCallback extends ResultCallback.Adapter<Frame> {
    private final StringBuilder out = new StringBuilder();

    @Override
    public void onNext(Frame frame) {
        String s = new String(frame.getPayload());
        if (s.contains("ERROR") || s.contains("WARN"))
        { 
            out.append(s);
        }
    }

    public String get() { return out.toString(); }
    // TODO Add support for ContainerClass, needs last log copied timestamp.
}