import React, { useEffect, useState } from "react";
import "./css/DiagnosticsView.css";
import RunningView from "./RunningView.jsx"
import { Tab, Stack, Table, Form, Button, Row, Col, Nav} from "react-bootstrap";
import { Line, Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    LineElement,
    BarElement,
    PointElement,
    LinearScale,
    CategoryScale,
    Tooltip,
    Legend
} from "chart.js";
import pattern from "patternomaly"

ChartJS.register(BarElement, LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

export default function DiagnosticsView({ containerID }) {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount
    const [diagnosticsList, setDiagnosticsList] = useState([]);
    const [runningChart, setRunningChart] = useState(null);
    const [error, setError] = useState(null);

    // --- Helper: Convert timestamp -> "x minutes ago" ---
    const timeAgo = (timestamp) => {
        const now = new Date();
        const then = new Date(timestamp);

        const diffMs = now - then;
        const diffSec = Math.floor(diffMs / 1000);
        const diffMin = Math.floor(diffSec / 60);
        const diffHr = Math.floor(diffMin / 60);
        const diffDay = Math.floor(diffHr / 24);

        if (diffSec < 60) return `${diffSec}s ago`;
        if (diffMin < 60) return `${diffMin}m ago`;
        if (diffHr < 24) return `${diffHr}h ago`;
        return `${diffDay}d ago`;
    };

    useEffect(() => {
        async function fetchDiagnosticsData() {
            try {
                const res = await fetch(`/api/data/diagnosticsdata/${containerID}`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);

                const json = await res.json();

                const diagnosticsList = json && !Array.isArray(json)
                    ? Object.values(json)
                    : Array.isArray(json)
                        ? json
                        : (json.companies || []);

                setDiagnosticsList(diagnosticsList);
                console.log(diagnosticsList);
            } catch (err) {
                setError(err.message || String(err));
            }
        }

        fetchDiagnosticsData();
    }, [containerID]);

    return (
        <div className="Yes">
            {error && <p style={{ color: "red" }}>Error: {error}</p>}

            <h2 style={{ marginLeft: "25%", marginTop: "2%", marginBottom: "2%" }}>
                <b>Diagnostics View</b>
            </h2>

            <Tab.Container id="DiagnosticsView" defaultActiveKey="running">
                <Row>
                    <Col sm={3}>
                        <Nav variant="pills" className="flex-column">
                            <Nav.Item>
                                <Nav.Link eventKey="running">Running</Nav.Link>
                                <Nav.Link eventKey="cpuUsage">CPU Usage</Nav.Link>
                                <Nav.Link eventKey="ramUsage">RAM Usage</Nav.Link>
                                <Nav.Link eventKey="diskUsage">Disk Usage</Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Col>
                    <Col sm={9}>
                        <Tab.Content>
                            <Tab.Pane eventKey="running">
                                <RunningView diagnosticsList={diagnosticsList}/>
                            </Tab.Pane>
                            <Tab.Pane eventKey="cpuUsage">
                                <p>CPU Usage</p>
                            </Tab.Pane>
                            <Tab.Pane eventKey="ramUsage">
                                <p>RAM Usage</p>
                            </Tab.Pane>
                            <Tab.Pane eventKey="diskUsage">
                                <p>Disk Usage</p>
                            </Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container>
        </div>
    );
}
