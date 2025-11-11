import React, { useState, useEffect } from "react";
import "./App.css";
import warningSmall from "./assets/warning128.png";
import { Navbar, Nav, Container, Row, Col, Table, Badge, Accordion, Tab, Tabs, ListGroup, Stack, Spinner, Alert, Image } from "react-bootstrap";
import { Routes, Route, Link } from "react-router-dom";
import Dashboard from "./Dashboard";
import DockerButton from "./DockerButton.jsx";
import NavRegions from "./modules/NavRegions.jsx";
import NavCompanies from "./modules/NavCompanies.jsx";

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

    // Selected company state (for the left-side company list)
    const [selectedCompanyKey, setSelectedCompanyKey] = useState(null);
    // Controlled tab state (derived from pathname by default)
    const [activeTab, setActiveTab] = useState(() => {
        const p = window.location.pathname.replace(/^\//, "");
        if (!p) return "Europe";
        const cleaned = p.replace(/[^a-zA-Z]/g, "");
        return cleaned ? cleaned.charAt(0).toUpperCase() + cleaned.slice(1) : "Europe";
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
        const slug = key.toLowerCase().replace(/\s+/g, "");
        // clear company selection when switching regions
        setSelectedCompanyKey(null);
        // push URL reflecting region (use /nav/<region>/)
        window.history.pushState({}, "", `/nav/${slug}/`);
        // call the region API
        fetchRegionData(slug);
    };

    // helper to create URL-friendly slugs
    const slugify = (s) =>
        String(s)
            .toLowerCase()
            .replace(/[^a-z0-9]+/g, "");

    // Called when a company is selected in the left panel
    const handleCompanySelect = (companyName, eventKey) => {
        if (!companyName) return;
        const companySlug = slugify(companyName);
        setSelectedCompanyKey(eventKey || null);
        const regionSlug = activeTab.toLowerCase().replace(/\s+/g, "");
        // push URL reflecting region and company
        window.history.pushState({}, "", `/nav/${regionSlug}/${companySlug}/`);
        // optionally fetch company-specific data here
    };
    // Keep activeTab in sync with browser navigation (back/forward)
    useEffect(() => {
        const onPop = () => {
            // parse paths like: /nav/<region>/ or /nav/<region>/<company>/
            const parts = window.location.pathname.split("/").filter(Boolean);
            if (parts[0] === "nav") {
                const regionSlug = parts[1] || "";
                // derive tab title from slug (match known tabs)
                const tabs = ["Europe", "North America", "Australia", "Asia"];
                const found = tabs.find((t) => t.toLowerCase().replace(/\s+/g, "") === regionSlug);
                const tab = found || "Europe";
                setActiveTab(tab);
                if (regionSlug) fetchRegionData(regionSlug);
                // company (if present)
                const companySlug = parts[2] || null;
                if (companySlug) {
                    // try to map companySlug to an eventKey in the current region
                    // we only control the North America example here; extend as needed
                    const companies = {
                        northamerica: [
                            { name: "CEGO", key: "first" },
                            { name: "MAERSK", key: "second" },
                            { name: "Danish Crown", key: "third" },
                        ],
                    };
                    const list = companies[regionSlug] || [];
                    const foundCompany = list.find((c) => slugify(c.name) === companySlug.toLowerCase());
                    if (foundCompany) {
                        setSelectedCompanyKey(foundCompany.key);
                    } else {
                        setSelectedCompanyKey(null);
                    }
                } else {
                    setSelectedCompanyKey(null);
                }
            } else {
                // default behaviour if path doesn't start with /nav/
                const p = window.location.pathname.replace(/^\//, "");
                const cleaned = p.replace(/[^a-zA-Z]/g, "");
                const tab = cleaned ? cleaned.charAt(0).toUpperCase() + cleaned.slice(1) : "Europe";
                setActiveTab(tab);
                const slug = cleaned.toLowerCase();
                if (slug) fetchRegionData(slug);
            }
        };
        window.addEventListener("popstate", onPop);
        // run once to initialize state from URL on mount
        onPop();
        return () => window.removeEventListener("popstate", onPop);
    }, []);

    const fetchDockerData = async () => {
        try {
            setLoading(true);
            const response = await fetch("http://localhost:5173/api/dockers/ctr-011");
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
                <Container style={{ maxWidth: "100%" }}>
                    <Navbar.Brand href="#home">Container Diagnostics Platform</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link as={Link} to="/dashboard">
                                Dashboard
                            </Nav.Link>
                            <Nav.Link href="/nav">Navigate</Nav.Link>
                            <Nav.Link href="/manage">Manage</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            <div className="Body">
                <Routes>
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route
                        path="/*"
                        element={
                            <>
                                {/* Main Content */}
                                <NavRegions />
                                {/* <NavCompanies regionID="fb4c6b1f-cc12-4d6b-a083-261bf21234a3" /> {/* Testing */}

                                <div id="dataViewerTest">
                                    <p id="dataTarget">Docker Data Viewer (Placeholder)</p>
                                    {/* <pre>{JSON.stringify(data, null, 2)}</pre> */}
                                    <DockerButton />
                                </div>
                            </>
                        }
                    />
                </Routes>
            </div>
        </div>
    );
};

export default App;
