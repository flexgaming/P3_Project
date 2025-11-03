package P3.Backend.Database;

public record Diagnostics(String timestamp, boolean running, double ramFree, double cpuFree, double diskUsageFree,
                          int threadCount, String processID, String status, String errorLogs) {
}
