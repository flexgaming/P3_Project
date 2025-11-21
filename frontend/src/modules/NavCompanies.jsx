import React, { useEffect, useState } from "react";
import { Alert, Tab, Row, Col, Nav} from "react-bootstrap";
import NavServers from "./NavServers";
import "../pages/css/Nav.css";

/**
 * NavCompanies component
 * - Fetches company names from /api/data/{regionID}/companies on mount
 * - Renders one ListGroup block per company name
 */
function NavCompanies({ regionID, regionName}) {
    const [companies, setCompanies] = useState([]);
    const [activeKey, setActiveKey] = useState(null);
    const [error, setError] = useState(null);
    // companies: array of company objects for this region
    // activeKey: currently selected Tab eventKey (companyName)
    // error: error message when fetching fails

    // Fetch companies for the given region on mount
    useEffect(() => {
        let mounted = true;
        async function fetchCompanies() {
            try {
                const res = await fetch(`/api/data/${regionID}/companies`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                // If backend responded with non-2xx => throw and show error
                const json = await res.json();
                console.log(json);

                // Extract region objects with both regionID and companyName
                // json structure: { "North America": { regionID: "NA", regionName: "North America" }, ... }
                // Support multiple shapes: object map or array
                const companyList = json && !Array.isArray(json)
                    ? Object.values(json)
                    : Array.isArray(json)
                        ? json
                        : (json.companies || []);
                if (mounted) {
                    setCompanies(companyList);
                    // set first tab active by companyName (matches Nav.Link/Tab.Pane eventKey)
                    setActiveKey(companyList.length ? String(companyList[0].companyName) : null);
                }
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        fetchCompanies();
        return () => {
            mounted = false;
        };
    }, [regionID]);

    // Render error if fetch failed
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        // Tab.Container to hold company tabs and their content
        <Tab.Container
            id={`${regionID}`}
            className="nav-companies-container"
            activeKey={activeKey}
            onSelect={(k) => setActiveKey(k)}
            defaultActiveKey={companies.length ? `${companies[0].companyName}` : null}
        >
            {/* Layout with Row and Cols */}
            <Row>
                <Col sm={3}>
                    <Nav variant="pills" className="flex-column">
                        <h4 style={{marginBottom: "28px"}}><b>Companies in {regionName}</b></h4>
                        {/* Render Nav.Items for each company */}
                        {companies.map(company => (
                            <Nav.Item key={company.companyID}>
                                <Nav.Link eventKey={`${company.companyName}`}>{`${company.companyName}`}</Nav.Link>
                            </Nav.Item>
                        ))}
                    </Nav>
                </Col>
                <Col sm={9}>
                    <Tab.Content>
                        {/* Render Tab.Pane for each company */}
                        {companies.map(company => (
                            <Tab.Pane eventKey={`${company.companyName}`} key={company.companyID}>
                                <h4><b>Servers & Containers in {company.companyName}</b></h4>
                                {/* Request and render servers and containers for this company */}
                                <NavServers companyID={company.companyID} />
                            </Tab.Pane>
                        ))}
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>
    );
}

export default NavCompanies;