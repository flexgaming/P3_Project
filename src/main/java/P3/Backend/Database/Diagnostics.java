package P3.Backend.Database;

import java.sql.Timestamp;

public record Diagnostics(Timestamp timestamp, boolean running, double ramFree, double cpuFree, double diskUsageFree,
                          int threadCount, String processID, String status, String errorLogs) {
}
