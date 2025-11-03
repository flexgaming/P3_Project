package P3.Backend.Database;

import java.util.ArrayList;

public class Region {
    private final int regionID;
    private final String name;

    private ArrayList<Company> companies = new ArrayList<>();

    public Region(int regionID, String name) {
        this.regionID = regionID;
        this.name = name;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    public int getRegionID() {
        return regionID;
    }

    public String getRegionName() {
        return name;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public Company getCompany(int companyID) {
        for (Company company : companies) {
            if (company.getCompanyID() == companyID) {
                return company;
            }
        }

        return null;
    }
}
