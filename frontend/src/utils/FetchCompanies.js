// Utility to fetch and normalize companies for a given region from the backend API.
// The backend may return several shapes (map keyed by id, array, or
// an object with a `companies` property). This helper returns a
// normalized array of company objects.
export async function getCompanies(regionID) {
    if (!regionID) return [];
    const res = await fetch(`/data/${regionID}/companies`);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const json = await res.json();

    // Normalize to an array of company objects
    const companyList = json && !Array.isArray(json)
        ? Object.values(json)
        : Array.isArray(json)
            ? json
            : (json.companies || []);

    return companyList;
}

export default getCompanies;
