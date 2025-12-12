import React, { useState, useEffect } from "react";
import "./App.css";
import "./pages/css/custom-bootstrap.scss";
import "./config/ConfigurationColors.css";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Routes, Route, Link, useNavigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard.jsx";
import DiagnosticsView from "./pages/DiagnosticsView.jsx";
import NavRegions from "./modules/NavRegions.jsx";
import { BsArrowLeftSquare } from "react-icons/bs";

const App = () => {
    const navigate = useNavigate();

    return (
        <div className="App">
            <Navbar expand="lg" sticky="top" className="full-bleed" id="main-navbar">
                <Container fluid>
                    <Navbar.Brand href="/dashboard">Container Diagnostics Platform</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto" style={{marginLeft: "10px"}}>
                            <Nav.Link as={Link} to="/dashboard">Dashboard</Nav.Link>
                            <Nav.Link as={Link} to="/nav">Navigate</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            <button
                type="button"
                onClick={() => navigate(-1)}
                aria-label="Go back"
                style={{
                    position: "fixed",
                    top: "65px",
                    left: "10px",
                    zIndex: 1000,
                    background: "#9ED3E8",
                    borderRadius: "6px",
                    border: "none",
                    cursor: "pointer",
                    padding: 0,
                }}
            >
                <BsArrowLeftSquare size={35} />
            </button>
            <div className="Body">
                <Routes>
                    <Route path="/*" element={<Dashboard/>} />
                    <Route path="/diagnosticsview/:containerID" element={<DiagnosticsView/>} />
                    <Route path="/nav" element={<NavRegions/>} />
                    <Route path="/nav/:regionName" element={<NavRegions/>} />
                    <Route path="/nav/:regionName/:companyName" element={<NavRegions/>} />
                </Routes>
            </div>
        </div>
    );
};

export default App;
