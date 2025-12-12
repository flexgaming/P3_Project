package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.Statistics;

/** This function is used to fetch get the first snapshot of the incoming stream.
     *
     * Hereby, setting the stats to be the latest statistics from the container.
     */
    public class SingleStatsCallback extends ResultCallbackTemplate<SingleStatsCallback, Statistics> {
        private Statistics stats;

        /** This function is used to as a sort of API to get the latest stream of data.
         *
         * It will stop when it has got the latest snapshot of data.
         *
         * @param statistics Is used to set all of the stats that the docker container has.
         */
        @Override
        public void onNext(Statistics statistics) {
            this.stats = statistics;
            try {
                this.close(); // stop after first snapshot
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /** This function is used to retrieve the latest result from the onNext function.
         *
         * @return Is a statistics object with all of the container stats data.
         */
        public Statistics getStats() {
            return stats;
        }
    }