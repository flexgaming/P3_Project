import "./css/DiagnosticsView.css";
import React, { useEffect, useState } from "react";
import { Tab, Row, Col, Nav} from "react-bootstrap";
import RunningView from "./RunningView.jsx";
import CpuView from "./CpuView.jsx";
import RamView from "./RamView.jsx";
import DiskUsageView from "./DiskUsageView.jsx";

export default function DiagnosticsView({ containerID }) {
    const [containerData, setContainerData] = useState([]);
    const [serverData, setServerData] = useState([]);
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

                const containerData = json;

                setContainerData(containerData);
                console.log(containerData);
            } catch (err) {
                setError(err.message || String(err));
            }
        }


        fetchDiagnosticsData();
    }, [containerID]);

    useEffect(() => {
        if (!containerData ||!containerData.containerData) return;

        async function fetchServerData() {
            try {
                const res = await fetch(`/api/data/serverdata/${containerData.containerData.serverReference}`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);

                const json = await res.json();

                const serverData = json;

                setServerData(serverData);
                console.log(serverData);
            } catch (err) {
                setError(err.message || String(err));
            }
        }

        fetchServerData();
    }, [containerData]);

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
                                <RunningView diagnosticsData={containerData.diagnosticsData} timeAgo={timeAgo}/>
                            </Tab.Pane>
                            <Tab.Pane eventKey="cpuUsage">
                                <CpuView containerData={containerData} serverData={serverData} timeAgo={timeAgo}/>
                            </Tab.Pane>
                            <Tab.Pane eventKey="ramUsage">
                                <RamView containerData={containerData} serverData={serverData} timeAgo={timeAgo}/>
                            </Tab.Pane>
                            <Tab.Pane eventKey="diskUsage">
                                <DiskUsageView containerData={containerData} serverData={serverData} timeAgo={timeAgo}/>
                            </Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container>
        </div>
    );
}
