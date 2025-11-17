import React, { useEffect, useState } from "react";
import { ListGroup, Badge, Alert } from "react-bootstrap";
import "../Dashboard.css";

/**
 * AddRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 */
function AddRegionsDashboard() {
    const [regions, setRegions] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        let mounted = true;
        async function fetchRegions() {
            try {
                const res = await fetch("/api/data/regions");
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();

                // Extract region objects with both regionID and regionName
                // json structure: { "North America": { regionID: "NA", regionName: "North America" }, ... }
                const regionList = Object.values(json);

                if (mounted) setRegions(regionList);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        fetchRegions();
        return () => {
            mounted = false;
        };
    }, []);

    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        <>
            {regions.map((region) => (
                <div className="region-cards p-2 w-100" id={`${region.regionID}`} key={region.regionID}>
                    <ListGroup className="shadow rounded-4">
                        <ListGroup.Item>
                            <h3><b>{region.regionName}</b></h3>
                            {/* <small className="text-muted">ID: {region.regionID}</small> */}
                        </ListGroup.Item>
                        <ListGroup.Item>
                            Active Containers: <Badge bg="primary">—</Badge> / <Badge bg="primary">—</Badge>
                        </ListGroup.Item>
                        <ListGroup.Item>
                            Companies: <Badge bg="primary">—</Badge>
                        </ListGroup.Item>
                        <ListGroup.Item>
                            Total uptime: <Badge bg="primary">—</Badge>
                        </ListGroup.Item>
                        <ListGroup.Item>
                            Errors last hour: <Badge bg="primary">—</Badge>
                        </ListGroup.Item>
                    </ListGroup>
                </div>
            ))}
        </>
    );
}

export default AddRegionsDashboard;
