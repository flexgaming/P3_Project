package P3.Backend;

import java.util.ArrayList;

import P3.Backend.Database.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // Run main using Spring Boot
        SpringApplication.run(App.class, args);

        Database database = new Database();
        // addDummyData(database);
        ArrayList<Region> regions = database.getRegions();
        //printDBData(regions);

        // Test getting diagnostics data for a specific container
        /* Container dockerTst = new Container("ctr-001");
        Container testData = database.getDiagnosticsData(dockerTst);
        System.out.println(testData.getDiagnosticsData()); */
    }

    private static void printDBData(ArrayList<Region> regions) {
        for (Region region : regions) {
            System.out.print("Region: " + region.getRegionID() + " ");
            System.out.println(region.getRegionName());
            for (Company company : region.getCompanies()) {
                System.out.print("Company: " + company.getCompanyID() + " ");
                System.out.println(company.getCompanyName());
                for (Server server : company.getServers()) {
                    System.out.print("Server: " + server.getServerID() + " ");
                    System.out.print(server.getRamTotal() + " ");
                    System.out.print(server.getCpuTotal() + " ");
                    System.out.println(server.getDiskUsageTotal());
                    for (Container container : server.getContainers()) {
                        System.out.println("Container: " + container.getContainerID());
                        for (Diagnostics diagnostics : container.getDiagnosticsData()) {
                            System.out.println("Diagnostics: " + diagnostics);
                        }
                    }
                }
            }
        }
    }

    private static void addDummyData(Database db) {
        db.addRegions(
                new String[] { "North America", "Europe", "Asia", "South America", "Australia" });
        db.addCompanies(
                new int[] { 1, 1, 2, 2, 3, 3, 4, 5 },
                new String[] { "TechNova Inc.", "CloudForge LLC", "EuroCloud GmbH", "Datastream Systems",
                        "AsiaNet Solutions", "PacificWare Co.", "Andes Data Corp.", "AussieCompute Ltd." });
        db.addServers(
                new String[] { "srv-101", "srv-102", "srv-201", "srv-301", "srv-302", "srv-401", "srv-501", "srv-601",
                        "srv-701", "srv-801" },
                new int[] { 1, 1, 2, 3, 3, 4, 5, 6, 7, 8 },
                new double[] { 128.0, 64.0, 96.0, 128.0, 64.0, 96.0, 64.0, 128.0, 96.0, 64.0 },
                new double[] { 64.0, 32.0, 48.0, 64.0, 32.0, 48.0, 32.0, 64.0, 48.0, 32.0 },
                new double[] { 4000.0, 2000.0, 3500.0, 4200.0, 2500.0, 3000.0, 2000.0, 5000.0, 3500.0, 2500.0 });
        db.addContainers(
                new String[] { "ctr-001", "ctr-002", "ctr-003", "ctr-004", "ctr-005", "ctr-006", "ctr-007",
                        "ctr-008", "ctr-009", "ctr-010", "ctr-011", "ctr-012", "ctr-013", "ctr-014", "ctr-015" },
                new String[] { "srv-101", "srv-101", "srv-102", "srv-201", "srv-301", "srv-301", "srv-302",
                        "srv-401", "srv-501", "srv-601", "srv-601", "srv-701", "srv-701", "srv-801", "srv-801" });
        db.addDiagnosticsBatch(
                new String[] {
                        "ctr-001", "ctr-001", "ctr-001",
                        "ctr-002", "ctr-002", "ctr-002",
                        "ctr-003", "ctr-003", "ctr-003",
                        "ctr-004", "ctr-004", "ctr-004",
                        "ctr-005", "ctr-005", "ctr-005",
                        "ctr-006", "ctr-006", "ctr-006",
                        "ctr-007", "ctr-007", "ctr-007",
                        "ctr-008", "ctr-008", "ctr-008",
                        "ctr-009", "ctr-009", "ctr-009",
                        "ctr-010", "ctr-010", "ctr-010",
                        "ctr-011", "ctr-011", "ctr-011",
                        "ctr-012", "ctr-012", "ctr-012",
                        "ctr-013", "ctr-013", "ctr-013",
                        "ctr-014", "ctr-014", "ctr-014",
                        "ctr-015", "ctr-015", "ctr-015"
                },
                new boolean[] {
                        true, true, false,
                        false, true, true,
                        true, true, false,
                        true, true, false,
                        true, true, true,
                        false, true, true,
                        true, true, false,
                        true, true, true,
                        false, true, true,
                        true, true, true,
                        false, true, true,
                        true, true, false,
                        true, true, false,
                        true, true, false,
                        true, true, true
                },
                new double[] {
                        8.0, 7.5, 6.0,
                        4.0, 6.0, 8.0,
                        16.0, 15.5, 10.0,
                        10.0, 9.8, 5.0,
                        12.0, 13.0, 14.0,
                        4.0, 8.0, 10.0,
                        6.0, 6.5, 5.0,
                        8.0, 8.1, 8.2,
                        3.0, 6.0, 7.0,
                        12.0, 11.8, 11.5,
                        5.0, 8.0, 9.0,
                        10.0, 10.5, 8.0,
                        9.0, 9.2, 7.0,
                        11.0, 10.8, 8.0,
                        12.0, 11.5, 11.0
                },
                new double[] {
                        4.0, 3.8, 3.0,
                        2.0, 3.0, 4.0,
                        8.0, 7.5, 5.0,
                        5.0, 4.9, 2.5,
                        6.0, 6.5, 7.0,
                        2.0, 4.0, 5.0,
                        3.0, 3.2, 2.5,
                        4.0, 4.0, 4.1,
                        1.5, 3.0, 3.5,
                        6.0, 5.9, 5.7,
                        2.5, 4.0, 4.5,
                        5.0, 5.2, 4.0,
                        4.5, 4.6, 3.5,
                        5.5, 5.4, 4.0,
                        6.0, 5.8, 5.5
                },
                new double[] {
                        500.0, 480.0, 400.0,
                        250.0, 300.0, 350.0,
                        1000.0, 980.0, 700.0,
                        600.0, 590.0, 400.0,
                        800.0, 850.0, 900.0,
                        300.0, 450.0, 500.0,
                        400.0, 410.0, 350.0,
                        500.0, 520.0, 530.0,
                        200.0, 300.0, 400.0,
                        800.0, 780.0, 770.0,
                        300.0, 450.0, 500.0,
                        600.0, 620.0, 500.0,
                        550.0, 560.0, 400.0,
                        700.0, 680.0, 500.0,
                        800.0, 780.0, 750.0
                },
                new int[] {
                        120, 118, 110,
                        90, 95, 105,
                        200, 198, 180,
                        160, 158, 130,
                        150, 155, 160,
                        100, 120, 130,
                        110, 115, 100,
                        120, 122, 124,
                        80, 90, 100,
                        150, 148, 145,
                        100, 120, 130,
                        140, 145, 130,
                        135, 138, 120,
                        160, 158, 140,
                        150, 145, 140
                },
                new String[] {
                        "proc-001A", "proc-001B", "proc-001C",
                        "proc-002A", "proc-002B", "proc-002C",
                        "proc-003A", "proc-003B", "proc-003C",
                        "proc-004A", "proc-004B", "proc-004C",
                        "proc-005A", "proc-005B", "proc-005C",
                        "proc-006A", "proc-006B", "proc-006C",
                        "proc-007A", "proc-007B", "proc-007C",
                        "proc-008A", "proc-008B", "proc-008C",
                        "proc-009A", "proc-009B", "proc-009C",
                        "proc-010A", "proc-010B", "proc-010C",
                        "proc-011A", "proc-011B", "proc-011C",
                        "proc-012A", "proc-012B", "proc-012C",
                        "proc-013A", "proc-013B", "proc-013C",
                        "proc-014A", "proc-014B", "proc-014C",
                        "proc-015A", "proc-015B", "proc-015C"
                },
                new String[] {
                        "Healthy", "Healthy", "Warning",
                        "Crashed", "Recovered", "Healthy",
                        "Healthy", "Healthy", "Warning",
                        "Healthy", "Healthy", "Crashed",
                        "Warning", "Healthy", "Healthy",
                        "Crashed", "Recovered", "Healthy",
                        "Healthy", "Healthy", "Warning",
                        "Healthy", "Healthy", "Healthy",
                        "Crashed", "Recovered", "Healthy",
                        "Healthy", "Healthy", "Healthy",
                        "Crashed", "Recovered", "Healthy",
                        "Healthy", "Healthy", "Warning",
                        "Healthy", "Healthy", "Warning",
                        "Healthy", "Healthy", "Healthy",
                        "Crashed", "Recovered", "Healthy"
                },
                new String[] {
                        "", "", "High memory usage",
                        "NullPointerException at line 42", "", "",
                        "", "", "CPU spike detected",
                        "", "", "Disk IO error",
                        "High CPU usage", "", "",
                        "OOMKilled", "", "",
                        "", "", "CPU spike detected",
                        "", "", "",
                        "Disk full", "", "",
                        "", "", "",
                        "Timeout error", "", "",
                        "", "", "Disk read latency",
                        "", "", "Network instability",
                        "", "", "CPU usage exceeded 90%",
                        "", "", ""
                });
    }
}
