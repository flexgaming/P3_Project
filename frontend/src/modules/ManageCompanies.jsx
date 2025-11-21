import React, { useEffect, useState } from "react";
import { Accordion, ListGroup, Badge, Spinner, Alert, Tabs, Tab} from "react-bootstrap";
import { getCompanies } from "../utils/FetchCompanies";
/**
 * AddRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 */
function ManageCompanies() {
    const [regions, setRegions] = useState([]);
    const [error, setError] = useState(null);
    // map of regionID -> array of companies
    const [companiesByRegion, setCompaniesByRegion] = useState({});

    useEffect(() => {
        let mounted = true;
        async function loadRegions() {
            try {
                // call shared helper
                const { getRegions } = await import("../utils/FetchRegions");
                const regionList = await getRegions();
                if (mounted) setRegions(regionList);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        loadRegions();
        return () => { mounted = false; };
    }, []);

    // When regions load, fetch companies for each region and store in companiesByRegion
    useEffect(() => {
        if (!regions || regions.length === 0) return;
        let mounted = true;

        async function fetchCompaniesForRegion(region) {
            try {
                const companyList = await getCompanies(region.regionID);
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

    // Render error if fetch failed
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        // Render Accordion of regions and their companies fetched from the backend
        <Accordion id="manage-companies-accordion" className="shadow rounded-4"> 
            {/* Iterate over regions to create Accordion items */}
            {regions.map((region, index) => (
                <Accordion.Item eventKey={String(index)} key={region.regionID}>
                    <Accordion.Header>{region.regionName}</Accordion.Header>
                    <Accordion.Body>
                        {/* ListGroup of companies for this region */}
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
