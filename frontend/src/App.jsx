import { useState } from "react";
import "./App.css";
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
} from "react-bootstrap";

// The main application component
const App = () => {
    // Sample data for the table
    const Regions = [
        { name: "North America", status: "Active" },
        { name: "Europe", status: "Inactive" },
        { name: "Asia", status: "Active" },
        { name: "Australia", status: "Active" },
    ];

    return (
        <div className="App">
            {/* Navbar */}
            <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
                <Container>
                    <Navbar.Brand href="#home">Dashboard</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link href="#home">Home</Nav.Link>
                            <Nav.Link href="#Regions">Regions</Nav.Link>
                            <Nav.Link href="#analytics">Analytics</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <Tabs
                id="regions-tabs"
                >
                <Tab eventKey="Europe" title="Europe">
                    <h3>Companies in Europe</h3>
                    <Container className="Companies-container">
                        <Accordion className="Companies-accordion">
                            <Accordion.Item eventKey="0">
                                <Accordion.Header>CEGO</Accordion.Header>
                                <Accordion.Body>
                                    <Row>
                                        <Col>
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
                                        <div className="vr" />
                                        <Col>
                                            <ListGroup className="lg1">
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
                                        <div className="vr" />
                                        <Col>
                                            <ListGroup className="lg1">
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
                                    
                                    

                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="1">
                                <Accordion.Header>NETIP</Accordion.Header>
                                <Accordion.Body>
                                    Lorem ipsum dolor sit amet, consectetur adipiscing.
                                </Accordion.Body>
                            </Accordion.Item>
                        </Accordion>
                    </Container>
                </Tab>
                <Tab eventKey="North America" title="North America">
                    <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                        <Row>
                            <Col sm={3}>
                            <Nav variant="pills" className="flex-column">
                                <Nav.Item>
                                <Nav.Link eventKey="first">CEGO</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                <Nav.Link eventKey="second">MAERSK</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                <Nav.Link eventKey="third">Danish Crown</Nav.Link>
                                </Nav.Item>
                            </Nav>
                            </Col>
                            <Col sm={9}>
                            <Tab.Content>
                                <Tab.Pane eventKey="first">
                                    <Row>
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                    <br></br>
                                    <Row>
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                    <br></br>
                                    <Row>
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                        <Col>
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
                                    <br></br>
                                    <Row>
                                        <Col>
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
                                        <Col>
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
                                </Tab.Pane>
                            </Tab.Content>
                            </Col>
                        </Row>
                    </Tab.Container>
                </Tab>
                <Tab eventKey="South America" title="South America">
                    Tab content for South America
                    <Stack direction="horizontal" gap={3}>
                        <div className="p-2">
                            <ListGroup defaultActiveKey="#link1">
                                <ListGroup.Item action href="#link1">
                                    Company 1
                                </ListGroup.Item>
                                <ListGroup.Item action href="#link2">
                                    Company 2
                                </ListGroup.Item>
                                <ListGroup.Item action href="#link3">
                                    Company 3
                                </ListGroup.Item>
                            </ListGroup>
                        </div>
                        <div className="vr" />
                        <div className="p-2 ms-auto">
                            <ListGroup defaultActiveKey="#link1">
                                <ListGroup.Item action href="#link1">
                                    Server 1
                                </ListGroup.Item>
                                <ListGroup.Item action href="#link2">
                                    Server 2
                                </ListGroup.Item>
                                <ListGroup.Item action href="#link3">
                                    Server 3
                                </ListGroup.Item>
                            </ListGroup>
                        </div>
                        <div className="vr" />
                        <div className="p-2">
                            <ListGroup defaultActiveKey="#link1">
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
                        </div>
                    </Stack>
                </Tab>
                <Tab eventKey="Australia" title="Australia">
                    Tab content for Australia
                </Tab>
                <Tab eventKey="Asia" title="Asia">
                    Tab content for Asia
                </Tab>
            </Tabs>
            {/* Main Content */}
            
            <div className="card p-3">
                <h3>Docker Data Viewer (Placeholder)</h3>
                {/* <DockerButton /> */}
            </div>
        </div>
    );
};

export default App;
