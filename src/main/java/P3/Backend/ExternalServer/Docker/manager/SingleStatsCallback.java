package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.Statistics;

    public class SingleStatsCallback extends ResultCallbackTemplate<SingleStatsCallback, Statistics> {
        private Statistics stats;

        @Override
        public void onNext(Statistics statistics) {
            this.stats = statistics;
            try {
                this.close(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Statistics getStats() {
            return stats;
        }
    }