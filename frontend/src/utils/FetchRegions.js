



export async function getRegions() {
    const res = await fetch("/data/regions");
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const json = await res.json();

    
    const regionList = json && !Array.isArray(json)
        ? Object.values(json)
        : Array.isArray(json)
            ? json
            : (json.regions || []);

    return regionList;
}

export default getRegions;
