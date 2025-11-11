
<div className="Body">
    <Routes>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route
            path="/*"
            element={
                <>
                    {/* Main Content */}
                    <Tabs id="nav-region-tabs" activeKey={activeTab} onSelect={handleTabSelect} justify>
                        <Tab eventKey="Europe" title="Europe">
                            {/* This is the Europe tab */}
                            <h3>Companies in Europe</h3>
                        </Tab>
                        <Tab eventKey="North America" title="North America">
                            {/* This is the North America tab */}
                            <Tab.Container id="left-tabs-example" activeKey={selectedCompanyKey || "first"} onSelect={(k) => setSelectedCompanyKey(k)}>
                                <Row>
                                    <Col sm={3} className="Company-List-Column">
                                        <Nav variant="pills" className="flex-column">
                                            <Nav.Item className="Company-List-Item">
                                                <Nav.Link eventKey="first" className="Company-List-Link" onClick={() => handleCompanySelect("CEGO", "first")}>
                                                    CEGO
                                                </Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item className="Company-List-Item">
                                                <Nav.Link
                                                    eventKey="second"
                                                    className="Company-List-Link"
                                                    onClick={() => handleCompanySelect("MAERSK", "second")}>
                                                    MAERSK
                                                </Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item className="Company-List-Item">
                                                <Nav.Link
                                                    eventKey="third"
                                                    className="Company-List-Link"
                                                    onClick={() => handleCompanySelect("Danish Crown", "third")}>
                                                    Danish Crown
                                                </Nav.Link>
                                            </Nav.Item>
                                        </Nav>
                                    </Col>
                                    <Col sm={9}>
                                        <Tab.Content>
                                            <Tab.Pane eventKey="first">
                                                <Row>
                                                    <Col className="Server-Column">
                                                        <ListGroup className="lg1">
                                                            <ListGroup.Item disabled className="Server-Header">
                                                                <Stack direction="horizontal" gap={2}>
                                                                    <div>Server 1</div>
                                                                    <div className="ms-auto">Active containers:</div>
                                                                    <div>3</div>
                                                                </Stack>
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link1" variant="success">
                                                                <Stack direction="horizontal" gap={0}>
                                                                    <div className="Container-Name-Container">Container 1</div>
                                                                    <div className="ms-auto">
                                                                        <Image
                                                                            src={warningSmall}
                                                                            id="ctr-001-Warning"
                                                                            alt="warning"
                                                                            width="30px"
                                                                            height="30px"
                                                                            hidden
                                                                        />
                                                                    </div>
                                                                    <div className="ms-auto">Uptime:</div>
                                                                    <div className="fixed-status">100%</div>
                                                                </Stack>
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link2" variant="danger">
                                                                <Stack direction="horizontal" gap={0}>
                                                                    <div className="Container-Name-Container">Bandit</div>
                                                                    <div className="ms-auto">
                                                                        <Image
                                                                            src={warningSmall}
                                                                            id="ctr-002-Warning"
                                                                            alt="warning"
                                                                            width="30px"
                                                                            height="30px"
                                                                        />
                                                                    </div>
                                                                    <div className="ms-auto">Uptime:</div>
                                                                    <div className="fixed-status">40%</div>
                                                                </Stack>
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link3" variant="warning">
                                                                <Stack direction="horizontal" gap={0}>
                                                                    <div className="Container-Name-Container">Container 3</div>
                                                                    <div className="ms-auto">
                                                                        <Image
                                                                            src={warningSmall}
                                                                            id="ctr-003-Warning"
                                                                            alt="warning"
                                                                            width="30px"
                                                                            height="30px"
                                                                        />
                                                                    </div>
                                                                    <div className="ms-auto">Uptime:</div>
                                                                    <div className="fixed-status">76%</div>
                                                                </Stack>
                                                            </ListGroup.Item>
                                                        </ListGroup>
                                                    </Col>
                                                    <Col className="Server-Column">
                                                        <ListGroup className="lg1">
                                                            <ListGroup.Item disabled>Server 2</ListGroup.Item>
                                                            <ListGroup.Item action href="#link1" variant="warning">
                                                                Container 1
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link2" variant="danger">
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
                                                </Row>
                                            </Tab.Pane>
                                            <Tab.Pane eventKey="second">
                                                <Row>
                                                    <Col className="Server-Column">
                                                        <ListGroup className="lg1">
                                                            <ListGroup.Item disabled>Server 1</ListGroup.Item>
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
                                            </Tab.Pane>
                                            <Tab.Pane eventKey="third">
                                                {" "}
                                                {/* Danish Crown */}
                                                <Row>
                                                    <Col className="Server-Column">
                                                        <ListGroup className="lg1">
                                                            <ListGroup.Item disabled>Server 1</ListGroup.Item>
                                                            <ListGroup.Item action href="#link1">
                                                                Container 1
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link2">
                                                                Container 2
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link3">
                                                                Container 3
                                                            </ListGroup.Item>
                                                            <ListGroup.Item action href="#link4">
                                                                Container 4
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
                            {/* This is the Australia tab */}
                            Tab content for Australia
                        </Tab>
                        <Tab eventKey="Asia" title="Asia">
                            {/* This is the Asia tab */}
                            Tab content for Asia
                        </Tab>
                    </Tabs>

                    
                </>
            }
        />
    </Routes>
</div>
