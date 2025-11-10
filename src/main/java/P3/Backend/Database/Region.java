package P3.Backend.Database;

import java.util.ArrayList;

public class Region {
    private final int regionID;
    private final String name;

    private final ArrayList<Company> companies = new ArrayList<>();

    public Region(int regionID, String name) {
        this.regionID = regionID;
        this.name = name;
    }

    public int getRegionID() {
        return regionID;
    }

    public String getRegionName() {
        return name;
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
    public Company getCompany(int companyID) {
        for (Company company : companies) {
            if (company.getCompanyID() == companyID) {
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
