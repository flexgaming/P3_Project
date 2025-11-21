import React, { useEffect, useState } from "react";
import { ListGroup, Alert} from "react-bootstrap";
import { getRegions } from "../utils/FetchRegions";


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
        async function loadRegions() {
            try {
                const regionList = await getRegions();
                if (mounted) setRegions(regionList);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        // Fetch regions for the given region on mount
        loadRegions();
        return () => { mounted = false; };
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