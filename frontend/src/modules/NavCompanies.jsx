import React, { useEffect, useState } from "react";
import { Alert, Tab, Row, Col, Nav} from "react-bootstrap";
import NavServers from "./NavServers";
import "../pages/css/Nav.css";
import { getCompanies } from "../utils/FetchCompanies";
import { useNavigate } from "react-router-dom";


function NavCompanies({ regionID, regionName, selectedCompanyName }) {
    const [companies, setCompanies] = useState([]);
    const [activeKey, setActiveKey] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    
    
    

    
    useEffect(() => {
        let mounted = true;

        async function loadCompanies() {
            try {
                const companyList = await getCompanies(regionID);
                if (mounted) {
                    setCompanies(companyList);
                    
                    if (selectedCompanyName) {
                        const requested = String(selectedCompanyName).toLowerCase();
                        const match = companyList.find(c => {
                            const name = String(c.companyName || "").toLowerCase();
                            const slug = name.replace(/\s+/g, "-");
                            return name === requested || slug === requested;
                        });
                        setActiveKey(match ? String(match.companyName) : (companyList.length ? String(companyList[0].companyName) : null));
                    } else {
                        setActiveKey(companyList.length ? String(companyList[0].companyName) : null);
                    }
                }
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        loadCompanies();
        return () => { mounted = false; };
    }, [regionID, selectedCompanyName]);

    
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        
        <Tab.Container
            id={`${regionID}`}
            className="nav-companies-container"
            activeKey={activeKey}
            onSelect={(k) => setActiveKey(k)}
            defaultActiveKey={companies.length ? `${companies[0].companyName}` : null}
        >
            {}
            <Row>
                <Col sm={3}>
                    <Nav variant="pills" className="flex-column">
                        <h4 style={{marginBottom: "28px"}}><b>Companies in {regionName}</b></h4>
                        {}
                        {companies.map(company => (
                            <Nav.Item key={company.companyID}>
                                <Nav.Link
                                    eventKey={`${company.companyName}`}
                                    onClick={() => {
                                        const companySlug = String(company.companyName || "").toLowerCase().replace(/\s+/g, "-");
                                        const regionSlug = String(regionName || "").toLowerCase().replace(/\s+/g, "-");
                                        
                                        navigate(`/nav/${regionSlug}/${companySlug}`);
                                        setActiveKey(company.companyName);
                                    }}
                                >
                                    {`${company.companyName}`}
                                </Nav.Link>
                            </Nav.Item>
                        ))}
                    </Nav>
                </Col>
                <Col sm={9}>
                    <Tab.Content>
                        {}
                        {companies.map(company => (
                            <Tab.Pane eventKey={`${company.companyName}`} key={company.companyID}>
                                <h4><b>Servers & Containers in {company.companyName}</b></h4>
                                {}
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