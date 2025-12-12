package P3.Backend;

import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        Database database = new Database();

    }

    private static void printData(Database database) {
        JSONObject regions = database.getRegions();
        JSONObject companies = database.getCompanies(regions.getJSONObject("North America").getString("regionID"));
        JSONObject servers = database.getServers(companies.getJSONObject("TechNova Inc.").getString("companyID"));
        JSONObject containers = database.getContainers(companies.getJSONObject("TechNova Inc.").getString("companyID"));
        JSONObject diagnosticsData = database.getDiagnosticsData(companies.getJSONObject("TechNova Inc.").getString("companyID"),
                                                            Constants.DIAGNOSTICS_TIME_SCOPE_LABEL);
        JSONObject allCompanyData = database.getRecentCompanyData(companies.getJSONObject("TechNova Inc.").getString("companyID"));
        System.out.println(allCompanyData.toString(4));
    }

    private static void addDummyData(Database database) {
        database.addRegions(
                new String[]{"North America", "Europe", "Asia", "South America", "Australia"}
        );
        JSONObject regions = database.getRegions();

        database.addCompanies(
                new String[]{
                        regions.getJSONObject("North America").getString("regionID"),
                        regions.getJSONObject("North America").getString("regionID"),
                        regions.getJSONObject("Europe").getString("regionID"),
                        regions.getJSONObject("Europe").getString("regionID"),
                        regions.getJSONObject("Asia").getString("regionID"),
                        regions.getJSONObject("Asia").getString("regionID"),
                        regions.getJSONObject("South America").getString("regionID"),
                        regions.getJSONObject("Australia").getString("regionID")
                },
                new String[]{"TechNova Inc.", "CloudForge LLC", "EuroCloud GmbH", "Datastream Systems",
                        "AsiaNet Solutions", "PacificWare Co.", "Anders Data Corp.", "AussieCompute Ltd."
                }
        );
        
        database.addServers(
                new String[] {
                        database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("TechNova Inc.").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("TechNova Inc.").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("CloudForge LLC").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("EuroCloud GmbH").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("EuroCloud GmbH").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("Datastream Systems").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("Asia").getString("regionID")).getJSONObject("AsiaNet Solutions").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("Asia").getString("regionID")).getJSONObject("PacificWare Co.").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("South America").getString("regionID")).getJSONObject("Anders Data Corp.").getString("companyID"),
                        database.getCompanies(regions.getJSONObject("Australia").getString("regionID")).getJSONObject("AussieCompute Ltd.").getString("companyID"),
                },
                new String[] { "AetherCore", "NovaNode", "QuantumHub", "IronPeak", "EchoForge", "SolarisGate",
                        "ObsidianRealm", "CrystalPulse", "VortexNet", "TitanVale" }
        );
        database.addContainers(
                new String[] { "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c", "ctr-002", "ctr-003", "ctr-004", "ctr-005", "ctr-006", "ctr-007",
                        "ctr-008", "ctr-009", "ctr-010", "ctr-011", "ctr-012", "ctr-013", "ctr-014", "ctr-015" },
                new String[] {
                        database.getServers(database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("TechNova Inc.").getString("companyID")).getJSONObject("AetherCore").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("TechNova Inc.").getString("companyID")).getJSONObject("AetherCore").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("TechNova Inc.").getString("companyID")).getJSONObject("NovaNode").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("North America").getString("regionID")).getJSONObject("CloudForge LLC").getString("companyID")).getJSONObject("QuantumHub").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("EuroCloud GmbH").getString("companyID")).getJSONObject("IronPeak").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("EuroCloud GmbH").getString("companyID")).getJSONObject("IronPeak").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("EuroCloud GmbH").getString("companyID")).getJSONObject("EchoForge").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Europe").getString("regionID")).getJSONObject("Datastream Systems").getString("companyID")).getJSONObject("SolarisGate").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Asia").getString("regionID")).getJSONObject("AsiaNet Solutions").getString("companyID")).getJSONObject("ObsidianRealm").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Asia").getString("regionID")).getJSONObject("PacificWare Co.").getString("companyID")).getJSONObject("CrystalPulse").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Asia").getString("regionID")).getJSONObject("PacificWare Co.").getString("companyID")).getJSONObject("CrystalPulse").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("South America").getString("regionID")).getJSONObject("Anders Data Corp.").getString("companyID")).getJSONObject("VortexNet").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("South America").getString("regionID")).getJSONObject("Anders Data Corp.").getString("companyID")).getJSONObject("VortexNet").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Australia").getString("regionID")).getJSONObject("AussieCompute Ltd.").getString("companyID")).getJSONObject("TitanVale").getString("serverID"),
                        database.getServers(database.getCompanies(regions.getJSONObject("Australia").getString("regionID")).getJSONObject("AussieCompute Ltd.").getString("companyID")).getJSONObject("TitanVale").getString("serverID")
                },
                new String[] { "blue_whale", "iron_squid", "frosty_mongoose", "crimson_fox", "silent_panda",
                        "cosmic_turtle", "rapid_lynx", "shadow_otter", "amber_crow", "lunar_badger", "mystic_serpent",
                        "glacial_hawk", "rusty_wombat", "silver_iguana", "electric_ferret" }
        );
        database.addDiagnosticsBatch(
                new String[] {
                        "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c",
                        "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c",
                        "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c","40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c",
                        "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c", "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c", "40641bf7b8e28599fb9bfb1a8e44be2222eae9336bd6360b31f42520f15fd65c",
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
                        true, false, true, false, true,
                        true, false, true, false, true,
                        true, false, true, false, true,
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
                new Long[] {
                        1L, 2L, 3L, 4L, 5L,
                        6L, 7L, 8L, 9L, 10L,
                        11L, 12L, 13L, 14L, 15L,
                        8L, 7L, 6L,
                        4L, 6L, 8L,
                        16L, 15L, 10L,
                        10L, 9L, 5L,
                        12L, 13L, 14L,
                        4L, 8L, 10L,
                        6L, 6L, 5L,
                        8L, 8L, 8L,
                        3L, 6L, 7L,
                        12L, 11L, 11L,
                        5L, 8L, 9L,
                        10L, 10L, 8L,
                        9L, 9L, 7L,
                        11L, 10L, 8L,
                        12L, 11L, 11L
                },
                new Long[] {
                        1L, 2L, 3L, 4L, 5L,
                        6L, 7L, 8L, 9L, 10L,
                        11L, 12L, 13L, 14L, 15L,
                        8L, 7L, 6L,
                        4L, 6L, 8L,
                        16L, 15L, 10L,
                        10L, 9L, 5L,
                        12L, 13L, 14L,
                        4L, 8L, 10L,
                        6L, 6L, 5L,
                        8L, 8L, 8L,
                        3L, 6L, 7L,
                        12L, 11L, 11L,
                        5L, 8L, 9L,
                        10L, 10L, 8L,
                        9L, 9L, 7L,
                        11L, 10L, 8L,
                        12L, 11L, 11L
                },
                new Double[] {
                        0.5,0.6,0.7,0.8,0.9,
                        1.0,1.1,1.2,1.3,1.4,
                        1.5,1.6,1.7,1.8,1.9,
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
                new Double[] {
                        0.5,0.6,0.7,0.8,0.9,
                        1.0,1.1,1.2,1.3,1.4,
                        1.5,1.6,1.7,1.8,1.9,
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
                new Long[] {
                        100L,110L,120L,130L,140L,
                        150L,160L,170L,180L,190L,
                        200L,210L,220L,230L,240L,

                        500L, 480L, 400L,
                        250L, 300L, 350L,
                        1000L, 980L, 700L,
                        600L, 590L, 400L,
                        800L, 850L, 900L,
                        300L, 450L, 500L,
                        400L, 410L, 350L,
                        500L, 520L, 530L,
                        200L, 300L, 400L,
                        800L, 780L, 770L,
                        300L, 450L, 500L,
                        600L, 620L, 500L,
                        550L, 560L, 400L,
                        700L, 680L, 500L,
                        800L, 780L, 750L
                },
                new Long[] {
                        100L,110L,120L,130L,140L,
                        150L,160L,170L,180L,190L,
                        200L,210L,220L,230L,240L,

                        500L, 480L, 400L,
                        250L, 300L, 350L,
                        1000L, 980L, 700L,
                        600L, 590L, 400L,
                        800L, 850L, 900L,
                        300L, 450L, 500L,
                        400L, 410L, 350L,
                        500L, 520L, 530L,
                        200L, 300L, 400L,
                        800L, 780L, 770L,
                        300L, 450L, 500L,
                        600L, 620L, 500L,
                        550L, 560L, 400L,
                        700L, 680L, 500L,
                        800L, 780L, 750L
                },
                new int[] {
                        10,11,12,13,14,
                        15,16,17,18,19,
                        20,21,22,23,24,
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
                        "Healthy","Warning","Crashed","Recovered","Healthy",
                        "Warning","Healthy","Crashed","Healthy","Recovered",
                        "Warning","Healthy","Crashed","Healthy","Warning",
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
                new JSONObject[] {
                        new JSONObject().put("value", "Small pp"),
                        null,
                        new JSONObject().put("value", "Chicken"),
                        null,
                        null,

                        null,
                        new JSONObject().put("value", "Butter"),
                        null,
                        new JSONObject().put("value", "No maidens"),
                        null,

                        null,
                        new JSONObject().put("value", "Deep Depression"),
                        new JSONObject().put("value", "France"),
                        null,
                        null,

                        null,
                        null,
                        new JSONObject().put("value", "High memory usage"),

                        new JSONObject().put("value", "NullPointerException at line 42"),
                        null,
                        null,

                        null,
                        null,
                        new JSONObject().put("value", "CPU spike detected"),

                        null,
                        null,
                        new JSONObject().put("value", "Disk IO error"),

                        new JSONObject().put("value", "High CPU usage"),
                        null,
                        null,

                        new JSONObject().put("value", "OOMKilled"),
                        null,
                        null,

                        null,
                        null,
                        new JSONObject().put("value", "CPU spike detected"),

                        null,
                        null,
                        null,

                        new JSONObject().put("value", "Disk full"),
                        null,
                        null,

                        null,
                        null,
                        null,

                        new JSONObject().put("value", "Timeout error"),
                        null,
                        null,

                        null,
                        null,
                        new JSONObject().put("value", "Disk read latency"),

                        null,
                        null,
                        new JSONObject().put("value", "Network instability"),

                        null,
                        null,
                        new JSONObject().put("value", "CPU usage exceeded 90%"),

                        null,
                        null,
                        null,
                }
        );
    }
}
