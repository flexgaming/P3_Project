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
                new String[] { "srv-101", "srv-101", "srv-102", "srv-201", "srv-301", "srv-301", "srv-302",
                        "srv-401", "srv-501", "srv-601", "srv-601", "srv-701", "srv-701", "srv-801", "srv-801" },
                new String[] { "blue_whale", "iron_squid", "frosty_mongoose", "crimson_fox", "silent_panda",
                        "cosmic_turtle", "rapid_lynx", "shadow_otter", "amber_crow", "lunar_badger", "mystic_serpent",
                        "glacial_hawk", "rusty_wombat", "silver_iguana", "electric_ferret" }
        );


















































































































































































































































































































    }
}
