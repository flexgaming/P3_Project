package P3.Backend.Database;

import java.sql.Timestamp;

/*
    A record has record components that are declared in the header. These components are immutable, which means that
    they cannot be changed after being declared. This works perfectly for the diagnostics data, as this only contains
    data that should not be changed after declaration.
*/
public record Diagnostics(Timestamp timestamp, boolean running, double ramFree, double cpuFree, double diskUsageFree,
                          int threadCount, String processID, String status, String errorLogs) {
}
