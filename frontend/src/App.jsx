import React, { useState, useEffect } from "react";
import "./App.css";
import "./pages/css/custom-bootstrap.scss";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Routes, Route, Link } from "react-router-dom";
import Dashboard from "./pages/Dashboard.jsx";
import DiagnosticsView from "./pages/DiagnosticsView.jsx";
import NavRegions from "./modules/NavRegions.jsx";
import ManagePage from "./pages/Manage.jsx";

// The main application component
const App = () => {

    

    return (
        <div className="App">
            {/* Navbar */}
            <Navbar expand="lg" sticky="top" className="full-bleed" id="main-navbar">
                <Container fluid>
                    <Navbar.Brand href="/dashboard">Container Diagnostics Platform</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link as={Link} to="/dashboard">Dashboard</Nav.Link>
                            <Nav.Link as={Link} to="/nav">Navigate</Nav.Link>
                            <Nav.Link as={Link} to="/manage">Manage</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            <div className="Body">
                <Routes>
                    <Route path="/*" element={<Dashboard />} />
                    <Route path="/diagnosticsview/:containerID" element={<DiagnosticsView/>} />
                    <Route path="/manage" element={<ManagePage />} />
                    <Route path="/nav/*" element={<NavRegions />} />
                </Routes>
            </div>
        </div>
    );
};

export default App;
