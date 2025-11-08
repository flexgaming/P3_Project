import React, { useState, useEffect } from 'react';
import "./App.css";
import warningSmall from './assets/warning128.png';
import {
    Navbar,
    Nav,
    Container,
    Row,
    Col,
    Table,
    Badge,
    Accordion,
    Tab,
    Tabs,
    ListGroup,
    Stack,
    Spinner,
    Alert,
    Image
} from "react-bootstrap";


// The main application component
const App = () => {
    // State for general data fetched on mount
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // State for region-specific data fetched when a region tab is clicked
    const [regionData, setRegionData] = useState(null);
    const [regionLoading, setRegionLoading] = useState(false);
    const [regionError, setRegionError] = useState(null);

    // Controlled tab state (derived from pathname by default)
    const [activeTab, setActiveTab] = useState(() => {
        const p = window.location.pathname.replace(/^\//, '');
        if (!p) return 'Europe';
        const cleaned = p.replace(/[^a-zA-Z]/g, '');
        return cleaned ? cleaned.charAt(0).toUpperCase() + cleaned.slice(1) : 'Europe';
    });

    // Fetch region data for a region slug (e.g. 'europe', 'northamerica')
    const fetchRegionData = async (slug) => {
        try {
            setRegionLoading(true);
            setRegionError(null);
            // Adjust endpoint as needed for your backend
            const res = await fetch(`/api/regions/${slug}`);
            if (!res.ok) throw new Error(`HTTP error ${res.status}`);
            const json = await res.json();
            setRegionData(json);
        } catch (err) {
            setRegionError(err.message || String(err));
        } finally {
            setRegionLoading(false);
        }
    };

    // Called when a tab is selected
    const handleTabSelect = (key) => {
        if (!key) return;
        setActiveTab(key);
        const slug = key.toLowerCase().replace(/\s+/g, '');
        // push URL without reloading
        window.history.pushState({}, '', `/${slug}`);
        // call the region API
        fetchRegionData(slug);
    };

    // Keep activeTab in sync with browser navigation (back/forward)
    useEffect(() => {
        const onPop = () => {
            const p = window.location.pathname.replace(/^\//, '');
            const cleaned = p.replace(/[^a-zA-Z]/g, '');
            const tab = cleaned ? cleaned.charAt(0).toUpperCase() + cleaned.slice(1) : 'Europe';
            setActiveTab(tab);
            // fetch for the tab derived from URL
            const slug = cleaned.toLowerCase();
            if (slug) fetchRegionData(slug);
        };
        window.addEventListener('popstate', onPop);
        return () => window.removeEventListener('popstate', onPop);
    }, []);

    const fetchDockerData = async () => {
    try {
        setLoading(true);
        const response = await fetch('http://localhost:5173/api/dockers/ctr-011');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const result = await response.json();
        setData(result);
        } catch (err) {
        setError(err.message);
        } finally {
        setLoading(false);
        }
    };

    useEffect(() => {
        fetchDockerData();
    }, []); // Runs once on mount; add deps for refetching

    if (loading) {
        return (
        <div className="d-flex justify-content-center mt-4">
            <Spinner animation="border" variant="primary" />
        </div>
        );
    }

    if (error) {
        return (
        <Alert variant="danger" className="mt-4">
            Error fetching data: {error}
        </Alert>
        );
    }

    return (
        <div className="App">
            {/* Navbar */}
            <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
                <Container style={{ maxWidth: '100%' }}>
                    <Navbar.Brand href="#home">Container Diagnostics Platform</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link href="/dashboard">Dashboard</Nav.Link>
                            <Nav.Link href="/navigate">Navigate</Nav.Link>
                            <Nav.Link href="/manage">Manage</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <div className="Body">
                {/* Main Content */}
                <Tabs id="nav-region-tabs" activeKey={activeTab} onSelect={handleTabSelect} justify>
                    <Tab eventKey="Europe" title="Europe">
                        <h3>Companies in Europe</h3>
                        
                    </Tab>
                    <Tab eventKey="North America" title="North America">
                        <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                            <Row>
                                <Col sm={3} className="Company-List-Column">
                                    <Nav variant="pills" className="flex-column">
                                        <Nav.Item className="Company-List-Item">
                                            <Nav.Link eventKey="first" className='Company-List-Link'>CEGO</Nav.Link>
                                        </Nav.Item>
                                        <Nav.Item className="Company-List-Item">
                                            <Nav.Link eventKey="second" className='Company-List-Link'>MAERSK</Nav.Link>
                                        </Nav.Item>
                                        <Nav.Item className="Company-List-Item">
                                            <Nav.Link eventKey="third" className='Company-List-Link'>Danish Crown</Nav.Link>
                                        </Nav.Item>
                                    </Nav>
                                </Col>
                                <Col sm={9}>
                                <Tab.Content>
                                    <Tab.Pane eventKey="first">
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled className="serverHeader">
                                                        <Stack direction="horizontal" gap={2}>
                                                            <div>Server 1</div>
                                                            <div className="ms-auto">Active containers:</div>
                                                            <div>3</div>
                                                        </Stack>
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1" className="containerItems" style={{ backgroundColor: '#5bfd69ff' }}>
                                                        <Stack direction="horizontal" gap={0}>
                                                            <div className="Container-Name-Container">Container 1</div>
                                                            <div className="ms-auto"><Image src={warningSmall} id="ctr-001-Warning" alt="warning" width="30px" height="30px" hidden/></div>
                                                            <div className="ms-auto">Uptime:</div>
                                                            <div className="fixed-status">100%</div>
                                                        </Stack>
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2" className="containerItems" style={{ backgroundColor: '#fd5b5bff' }}>
                                                        <Stack direction="horizontal" gap={0}>
                                                            <div className="Container-Name-Container">Bandit</div>
                                                            <div className="ms-auto"><Image src={warningSmall} id="ctr-002-Warning" alt="warning" width="30px" height="30px"/></div>
                                                            <div className="ms-auto">Uptime:</div>
                                                            <div className="fixed-status">40%</div>
                                                        </Stack>
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3" className="containerItems" style={{ backgroundColor: '#ffd621ff' }}>
                                                        <Stack direction="horizontal" gap={0}>
                                                            <div className="Container-Name-Container">Container 3</div>
                                                            <div className="ms-auto"><Image src={warningSmall} id="ctr-003-Warning" alt="warning" width="30px" height="30px"/></div>
                                                            <div className="ms-auto">Uptime:</div>
                                                            <div className="fixed-status">76%</div>
                                                        </Stack>
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 4
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 5
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 6
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 4
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 5
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="second">
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 4
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 5
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 6
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 4
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="third"> { /* Danish Crown */ }
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 4
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 5
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col className="Server-Column">
                                                <ListGroup className="lg1">
                                                    <ListGroup.Item disabled>
                                                        Server 6
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link1">
                                                        Container 1
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link2">
                                                        Container 2
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                    <ListGroup.Item action href="#link3">
                                                        Container 3
                                                    </ListGroup.Item>
                                                </ListGroup>
                                            </Col>
                                        </Row>
                                    </Tab.Pane>
                                </Tab.Content>
                                </Col>
                            </Row>
                        </Tab.Container>
                    </Tab>
                    <Tab eventKey="Australia" title="Australia">
                        Tab content for Australia
                    </Tab>
                    <Tab eventKey="Asia" title="Asia">
                        Tab content for Asia
                    </Tab>
                </Tabs>
            
            
                <div id="dataViewerTest">
                    <p id="dataTarget">Docker Data Viewer (Placeholder)</p>
                    <pre>{JSON.stringify(data, null, 2)}</pre>
                    {/* <DockerButton /> */}
                </div>
            </div>
        </div>
    );
};

export default App;
