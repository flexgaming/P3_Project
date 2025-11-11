package P3.Backend.Database;

import java.util.ArrayList;

public class Region {
    private final String regionID;
    private final String regionName;

    private final ArrayList<Company> companies = new ArrayList<>();

    public Region(String regionID, String regionName) {
        this.regionID = regionID;
        this.regionName = regionName;
    }

    public String getRegionID() {
        return regionID;
    }

    public String getRegionName() {
        return regionName;
    }

    /**
     * Get all companies in this region.
     * @return All companies in the region.
     */
    public ArrayList<Company> getCompanies() {
        return companies;
    }

    /**
     * Search for a company using the company ID.
     * @param companyID The ID of the company being searched for.
     * @return The Company object if it exists, otherwise null.
     */
    public Company getCompany(String companyID) {
        for (Company company : companies) {
            if (company.getCompanyID().equals(companyID)) {
                return company;
            }
        }

        return null;
    }

    /**
     * Adds a Company object to this region.
     * @param company The Company object being added to the region.
     */
    public void addCompany(Company company) {
        companies.add(company);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
