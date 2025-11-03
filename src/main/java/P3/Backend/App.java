package P3.Backend;

import java.util.ArrayList;

import P3.Backend.Database.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
        // Run main using Spring Boot
		// SpringApplication.run(App.class, args);

        Database db = new Database();
        addDummyData(db);
        printDBData(db);
	}

    private static void printDBData(Database db) {
        ArrayList<Region> regions = db.getRegions();

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
        // === REGIONS ===
        db.addRegion("North America");
        db.addRegion("Europe");
        db.addRegion("Asia");
        db.addRegion("South America");
        db.addRegion("Australia");

        // === COMPANIES ===
        db.addCompany(1, "TechNova Inc.");
        db.addCompany(1, "CloudForge LLC");
        db.addCompany(2, "EuroCloud GmbH");
        db.addCompany(2, "Datastream Systems");
        db.addCompany(3, "AsiaNet Solutions");
        db.addCompany(3, "PacificWare Co.");
        db.addCompany(4, "Andes Data Corp.");
        db.addCompany(5, "AussieCompute Ltd.");

        // === SERVERS ===
        db.addServer("srv-101", 1, 128.0, 64.0, 4000.0);
        db.addServer("srv-102", 1, 64.0, 32.0, 2000.0);
        db.addServer("srv-201", 2, 96.0, 48.0, 3500.0);
        db.addServer("srv-301", 3, 128.0, 64.0, 4200.0);
        db.addServer("srv-302", 3, 64.0, 32.0, 2500.0);
        db.addServer("srv-401", 4, 96.0, 48.0, 3000.0);
        db.addServer("srv-501", 5, 64.0, 32.0, 2000.0);
        db.addServer("srv-601", 6, 128.0, 64.0, 5000.0);
        db.addServer("srv-701", 7, 96.0, 48.0, 3500.0);
        db.addServer("srv-801", 8, 64.0, 32.0, 2500.0);

        // === CONTAINERS ===
        db.addContainer("ctr-001", "srv-101");
        db.addContainer("ctr-002", "srv-101");
        db.addContainer("ctr-003", "srv-102");
        db.addContainer("ctr-004", "srv-201");
        db.addContainer("ctr-005", "srv-301");
        db.addContainer("ctr-006", "srv-301");
        db.addContainer("ctr-007", "srv-302");
        db.addContainer("ctr-008", "srv-401");
        db.addContainer("ctr-009", "srv-501");
        db.addContainer("ctr-010", "srv-601");
        db.addContainer("ctr-011", "srv-601");
        db.addContainer("ctr-012", "srv-701");
        db.addContainer("ctr-013", "srv-701");
        db.addContainer("ctr-014", "srv-801");
        db.addContainer("ctr-015", "srv-801");

        // === DIAGNOSTICS ===
        // Each container has 3 diagnostic entries (timestamp order implied)

        // Container 001 diagnostics
        db.addDiagnostics("ctr-001", true, 8.0, 4.0, 500.0, 120, "proc-001A", "Healthy", "");
        db.addDiagnostics("ctr-001", true, 7.5, 3.8, 480.0, 118, "proc-001B", "Healthy", "");
        db.addDiagnostics("ctr-001", false, 6.0, 3.0, 400.0, 110, "proc-001C", "Warning", "High memory usage");

        // Container 002
        db.addDiagnostics("ctr-002", false, 4.0, 2.0, 250.0, 90, "proc-002A", "Crashed", "NullPointerException at line 42");
        db.addDiagnostics("ctr-002", true, 6.0, 3.0, 300.0, 95, "proc-002B", "Recovered", "");
        db.addDiagnostics("ctr-002", true, 8.0, 4.0, 350.0, 105, "proc-002C", "Healthy", "");

        // Container 003
        db.addDiagnostics("ctr-003", true, 16.0, 8.0, 1000.0, 200, "proc-003A", "Healthy", "");
        db.addDiagnostics("ctr-003", true, 15.5, 7.5, 980.0, 198, "proc-003B", "Healthy", "");
        db.addDiagnostics("ctr-003", false, 10.0, 5.0, 700.0, 180, "proc-003C", "Warning", "CPU spike detected");

        // Container 004
        db.addDiagnostics("ctr-004", true, 10.0, 5.0, 600.0, 160, "proc-004A", "Healthy", "");
        db.addDiagnostics("ctr-004", true, 9.8, 4.9, 590.0, 158, "proc-004B", "Healthy", "");
        db.addDiagnostics("ctr-004", false, 5.0, 2.5, 400.0, 130, "proc-004C", "Crashed", "Disk IO error");

        // Container 005
        db.addDiagnostics("ctr-005", true, 12.0, 6.0, 800.0, 150, "proc-005A", "Warning", "High CPU usage");
        db.addDiagnostics("ctr-005", true, 13.0, 6.5, 850.0, 155, "proc-005B", "Healthy", "");
        db.addDiagnostics("ctr-005", true, 14.0, 7.0, 900.0, 160, "proc-005C", "Healthy", "");

        // Container 006
        db.addDiagnostics("ctr-006", false, 4.0, 2.0, 300.0, 100, "proc-006A", "Crashed", "OOMKilled");
        db.addDiagnostics("ctr-006", true, 8.0, 4.0, 450.0, 120, "proc-006B", "Recovered", "");
        db.addDiagnostics("ctr-006", true, 10.0, 5.0, 500.0, 130, "proc-006C", "Healthy", "");

        // Container 007
        db.addDiagnostics("ctr-007", true, 6.0, 3.0, 400.0, 110, "proc-007A", "Healthy", "");
        db.addDiagnostics("ctr-007", true, 6.5, 3.2, 410.0, 115, "proc-007B", "Healthy", "");
        db.addDiagnostics("ctr-007", false, 5.0, 2.5, 350.0, 100, "proc-007C", "Warning", "CPU spike detected");

        // Container 008
        db.addDiagnostics("ctr-008", true, 8.0, 4.0, 500.0, 120, "proc-008A", "Healthy", "");
        db.addDiagnostics("ctr-008", true, 8.1, 4.0, 520.0, 122, "proc-008B", "Healthy", "");
        db.addDiagnostics("ctr-008", true, 8.2, 4.1, 530.0, 124, "proc-008C", "Healthy", "");

        // Container 009
        db.addDiagnostics("ctr-009", false, 3.0, 1.5, 200.0, 80, "proc-009A", "Crashed", "Disk full");
        db.addDiagnostics("ctr-009", true, 6.0, 3.0, 300.0, 90, "proc-009B", "Recovered", "");
        db.addDiagnostics("ctr-009", true, 7.0, 3.5, 400.0, 100, "proc-009C", "Healthy", "");

        // Container 010–015 similar pattern (for brevity)
        db.addDiagnostics("ctr-010", true, 12.0, 6.0, 800.0, 150, "proc-010A", "Healthy", "");
        db.addDiagnostics("ctr-010", true, 11.8, 5.9, 780.0, 148, "proc-010B", "Healthy", "");
        db.addDiagnostics("ctr-010", true, 11.5, 5.7, 770.0, 145, "proc-010C", "Healthy", "");

        db.addDiagnostics("ctr-011", false, 5.0, 2.5, 300.0, 100, "proc-011A", "Crashed", "Timeout error");
        db.addDiagnostics("ctr-011", true, 8.0, 4.0, 450.0, 120, "proc-011B", "Recovered", "");
        db.addDiagnostics("ctr-011", true, 9.0, 4.5, 500.0, 130, "proc-011C", "Healthy", "");

        db.addDiagnostics("ctr-012", true, 10.0, 5.0, 600.0, 140, "proc-012A", "Healthy", "");
        db.addDiagnostics("ctr-012", true, 10.5, 5.2, 620.0, 145, "proc-012B", "Healthy", "");
        db.addDiagnostics("ctr-012", false, 8.0, 4.0, 500.0, 130, "proc-012C", "Warning", "Disk read latency");

        db.addDiagnostics("ctr-013", true, 9.0, 4.5, 550.0, 135, "proc-013A", "Healthy", "");
        db.addDiagnostics("ctr-013", true, 9.2, 4.6, 560.0, 138, "proc-013B", "Healthy", "");
        db.addDiagnostics("ctr-013", false, 7.0, 3.5, 400.0, 120, "proc-013C", "Warning", "Network instability");

        db.addDiagnostics("ctr-014", true, 11.0, 5.5, 700.0, 160, "proc-014A", "Healthy", "");
        db.addDiagnostics("ctr-014", true, 10.8, 5.4, 680.0, 158, "proc-014B", "Healthy", "");
        db.addDiagnostics("ctr-014", false, 8.0, 4.0, 500.0, 140, "proc-014C", "Warning", "CPU usage exceeded 90%");

        db.addDiagnostics("ctr-015", true, 12.0, 6.0, 800.0, 150, "proc-015A", "Healthy", "");
        db.addDiagnostics("ctr-015", true, 11.5, 5.8, 780.0, 145, "proc-015B", "Healthy", "");
        db.addDiagnostics("ctr-015", true, 11.0, 5.5, 750.0, 140, "proc-015C", "Healthy", "");
    }
}
