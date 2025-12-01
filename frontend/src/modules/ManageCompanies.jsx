import React, { useEffect, useState } from "react";
import { Accordion, Container, Breadcrumb, Form, Button, ListGroup, Badge, Spinner, Alert, Tabs, Tab} from "react-bootstrap";
import { getCompanies } from "../utils/FetchCompanies";


/**
 * AddRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 */
function ManageCompanies({ regionID }) {
    const [companies, setCompanies] = useState([]);
    const [error, setError] = useState(null);

    // When regions load, fetch companies for each region and store in companiesByRegion
    useEffect(() => {
        let mounted = true;

        async function loadCompanies() {
            try {
                const companyList = await getCompanies(regionID);
                if (mounted) setCompanies(companyList);
            } catch(err) {
                if (mounted) setError(err.message || string(err));
            }
        }

        loadCompanies();
        return () => { mounted = false; };
    }, [regionID]);

    // Render error if fetch failed
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
            // Render ListGroup of regions fetched from the backend
            <ListGroup id="manage-companies-listgroup" className="shadow rounded-4">
    {/*             Logic for managing regions can be added here */}
                {companies.map((company) => (
                    <ListGroup.Item key={company.companyID} action href={`/manage/${regionID}/${company.companyID}`}>{company.companyName}</ListGroup.Item>
                ))}
            </ListGroup>
        );
}

export default ManageCompanies;
