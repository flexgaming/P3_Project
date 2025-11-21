import React, { useEffect, useState } from "react";
import { ListGroup, Alert} from "react-bootstrap";


/** * ManageRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 * - Additional logic for managing regions can be added
 */
function ManageRegions() {
    const [regions, setRegions] = useState([]);
    const [error, setError] = useState(null);

    // Fetch regions for the given region on mount
    useEffect(() => {
        let mounted = true;
        async function fetchRegions() {
            try {
                const res = await fetch("/api/data/regions");
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();

                // Extract region objects with both regionID and regionName
                // json structure: { "North America": { regionID: "NA", regionName: "North America" }, ... }
                // Support multiple shapes: object map or array
                const regionList = json && !Array.isArray(json)
                    ? Object.values(json)
                    : Array.isArray(json)
                        ? json
                        : (json.regions || []);

                if (mounted) {
                    setRegions(regionList);
                }
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        // Fetch regions for the given region on mount
        fetchRegions();
        return () => {
            mounted = false;
        };
    }, []);

    // Render error if fetch failed
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        // Render ListGroup of regions fetched from the backend
        <ListGroup id="manage-regions-listgroup" className="shadow rounded-4">
            {/* Logic for managing regions can be added here */}
            {regions.map((region) => (
                <ListGroup.Item key={region.regionID}>{region.regionName}</ListGroup.Item>
            ))}
        </ListGroup>
    );
}

export default ManageRegions;