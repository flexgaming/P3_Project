export async function getCompanies(regionID) {
    if (!regionID) return [];
    const res = await fetch(`/data/${regionID}/companies`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const json = await res.json();

    const companyList = json && !Array.isArray(json)
        ? Object.values(json)
        : Array.isArray(json)
            ? json
            : (json.companies || []);

    return companyList;
}

export default getCompanies;
