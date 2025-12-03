// Utility to fetch and normalize regions from the backend API.
// The backend may return several shapes (map keyed by id, array, or
// an object with a `regions` property). This helper returns a
// normalized array of region objects.
export async function getRegions() {
    const res = await fetch("/data/regions");
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const json = await res.json();

    // Normalize to an array of region objects
    const regionList = json && !Array.isArray(json)
        ? Object.values(json)
        : Array.isArray(json)
            ? json
            : (json.regions || []);

    return regionList;
}

export default getRegions;
