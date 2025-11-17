import React, { useEffect, useState } from "react";
import { ListGroup, Badge, Spinner, Alert, Tabs, Tab} from "react-bootstrap";
import NavCompanies from "./NavCompanies.jsx";
import "./Nav.css";
/**
 * AddRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 */
function NavRegions() {
    const [regions, setRegions] = useState([]);
    const [activeKey, setActiveKey] = useState(null);
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
                // Support multiple shapes: object map or array
                const regionList = json && !Array.isArray(json)
                    ? Object.values(json)
                    : Array.isArray(json)
                        ? json
                        : (json.regions || []);

                if (mounted) {
                    setRegions(regionList);
                    // set first tab active (string id)
                    setActiveKey(regionList.length ? String(regionList[0].regionID) : null);
                }
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
        <Tabs
            id="regions-tabs"
            activeKey={activeKey}
            onSelect={(k) => setActiveKey(k)}
            className="mb-3"
            mountOnEnter
            justify
        >
            {regions.map((region) => (
                <Tab eventKey={String(region.regionID)} title={String(region.regionName)} key={String(region.regionID)}>
                    <div className="p-3">
                        <NavCompanies regionID={region.regionID} />
                    </div>
                </Tab>
            ))}
        </Tabs>
    );
}

export default NavRegions;
