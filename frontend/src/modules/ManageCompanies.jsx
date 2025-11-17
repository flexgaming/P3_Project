import React, { useEffect, useState } from "react";
import { Accordion, ListGroup, Badge, Spinner, Alert, Tabs, Tab} from "react-bootstrap";
/**
 * AddRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 */
function ManageCompanies() {
    const [regions, setRegions] = useState([]);
    const [activeKey, setActiveKey] = useState(null);
    const [error, setError] = useState(null);
    // map of regionID -> array of companies
    const [companiesByRegion, setCompaniesByRegion] = useState({});

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

    // When regions load, fetch companies for each region and store in companiesByRegion
    useEffect(() => {
        if (!regions || regions.length === 0) return;
        let mounted = true;

        async function fetchCompaniesForRegion(region) {
            try {
                const res = await fetch(`/api/data/${region.regionID}/companies`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();

                const companyList = json && !Array.isArray(json)
                    ? Object.values(json)
                    : Array.isArray(json)
                        ? json
                        : (json.companies || []);

                if (mounted) {
                    setCompaniesByRegion(prev => ({ ...prev, [region.regionID]: companyList }));
                }
            } catch {
                // store empty array on error for that region to avoid repeated attempts
                if (mounted) setCompaniesByRegion(prev => ({ ...prev, [region.regionID]: [] }));
            }
        }

        regions.forEach(r => {
            // don't re-fetch if we already have data for this region
            if (!companiesByRegion[r.regionID]) fetchCompaniesForRegion(r);
        });

        return () => { mounted = false; };
    }, [regions, companiesByRegion]);

    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        <Accordion id="manage-companies-accordion" className="shadow rounded-4"> 
            {regions.map((region, index) => (
                <Accordion.Item eventKey={String(index)} key={region.regionID}>
                    <Accordion.Header>{region.regionName}</Accordion.Header>
                    <Accordion.Body>
                        <ListGroup className="rounded-4">
                            {(companiesByRegion[region.regionID] || []).map((company) => (
                                <ListGroup.Item key={company.companyID}>{company.companyName}</ListGroup.Item>
                            ))}
                        </ListGroup>
                    </Accordion.Body>
                </Accordion.Item>
            ))}
        </Accordion>
    );
}

export default ManageCompanies;
