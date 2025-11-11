import React, { useEffect, useState } from "react";
import { Alert, Tab, Row, Col, Nav} from "react-bootstrap";

/**
 * NavServers component
 * - Fetches company names from /api/data/{regionID}/companies on mount
 * - Renders one ListGroup block per company name
 */
function NavServers({ regionID, companyID }) {
    const [servers, setServers] = useState([]);
    const [activeKey, setActiveKey] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        let mounted = true;
        async function fetchServers() {
            try {
                const res = await fetch(`/api/data/${regionID}/${companyID}/contents`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
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
                    setServers(companyList);
                    // set first tab active by companyName (matches Nav.Link/Tab.Pane eventKey)
                    setActiveKey(companyList.length ? String(companyList[0].companyName) : null);
                }
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        fetchServers();
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
        <Tab.Container
            id={`${regionID}`}
            activeKey={activeKey}
            onSelect={(k) => setActiveKey(k)}
            defaultActiveKey={companies.length ? `${companies[0].companyName}` : null}
        >
            <Row>
            <Col sm={3}>
                <Nav variant="pills" className="flex-column">
                    {companies.map(company => (
                        <Nav.Item key={company.companyID}>
                            <Nav.Link eventKey={`${company.companyName}`}>{`${company.companyName}`}</Nav.Link>
                        </Nav.Item>
                    ))}
                </Nav>
            </Col>
            <Col sm={9}>
                <Tab.Content>
                    {companies.map(company => (
                        <Tab.Pane eventKey={`${company.companyName}`} key={company.companyID}>
                            <h4>{`Company: ${company.companyName}`}</h4>
                        </Tab.Pane>
                    ))}
                </Tab.Content>
            </Col>
            </Row>
        </Tab.Container>
    );
}

export default NavServers;