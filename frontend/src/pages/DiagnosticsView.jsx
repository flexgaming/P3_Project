import "./css/DiagnosticsView.css";
import React, { useEffect, useState, useRef, useCallback } from "react";
import { Tab, Row, Col, Nav, Dropdown, DropdownButton} from "react-bootstrap";
import Overview from "./DiagnosticsViews/Overview.jsx";
import RunningView from "./DiagnosticsViews/RunningView.jsx";
import CpuView from "./DiagnosticsViews/CpuView.jsx";
import RamView from "./DiagnosticsViews/RamView.jsx";
import DiskUsageView from "./DiagnosticsViews/DiskUsageView.jsx";
import ThreadCountView from "./DiagnosticsViews/ThreadCountView.jsx";
import { useParams } from "react-router-dom";

export default function DiagnosticsView() {
    const { containerID } = useParams();
    // No global timeframe — each view manages its own timeframe.
    const [containerData, setContainerData] = useState([]);
    const [serverData, setServerData] = useState([]);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState("overview");

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

    // Keep track of the last (containerID,timeFrame) we requested to avoid
    // sending duplicate identical requests when parent re-renders.
    const lastFetchRef = useRef(null);

    // Fetch diagnostics for a given timeframe. Views will call this when they
    // become active and whenever their local timeframe changes while active.
    const fetchDiagnosticsFor = useCallback(async (timeFrame) => {
        if (!containerID || !timeFrame) return;
        const key = `${containerID}|${timeFrame}`;
        if (lastFetchRef.current === key) return; // already fetched this exact range
        lastFetchRef.current = key;

        try {
            const res = await fetch(`/data/diagnosticsdata/${containerID}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ timeFrame }),
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            setContainerData(await res.json());
        } catch (err) {
            setError(err.message || String(err));
        }
    }, [containerID]);

    // Clear dedupe state when container changes so we will fetch fresh data.
    useEffect(() => {
        lastFetchRef.current = null;
    }, [containerID]);

    // On initial load (or when user navigates back to Overview), fetch the
    // diagnostics for the Overview tab so it doesn't stay in a perpetual
    // "Loading chart…" state. Use the default timeframe of 10 minutes for
    // overview initial load.
    useEffect(() => {
        if (activeTab === "overview") {
            fetchDiagnosticsFor("10minutes");
        }
    }, [activeTab, fetchDiagnosticsFor]);

    useEffect(() => {
        if (!containerData ||!containerData.containerData) return;

        async function fetchServerData() {
            try {
                const res = await fetch(`/data/serverdata/${containerData.containerData.serverReference}`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);

                setServerData(await res.json());
            } catch (err) {
                setError(err.message || String(err));
            }
        }

        fetchServerData();
    }, [containerData]);

    return (
        <div id="DiagnosticsView">
            {error && <p style={{ color: "red" }}>Error: {error}</p>}

            <h2 style={{ padding: "10px" }}>
                <b>Diagnostics View for {containerData?.containerData?.containerName || "loading..."}</b>
            </h2>

            <Tab.Container activeKey={activeTab} onSelect={(k) => setActiveTab(k)}>
                <Row>
                    <Col sm={3}>
                        <Nav variant="pills" className="flex-column" id="DiagnosticsView-tabs">
                            <Nav.Item>
                                <Nav.Link eventKey="overview">Overview</Nav.Link>
                                <Nav.Link eventKey="running">Running</Nav.Link>
                                <Nav.Link eventKey="cpuUsage">CPU Usage</Nav.Link>
                                <Nav.Link eventKey="ramUsage">RAM Usage</Nav.Link>
                                <Nav.Link eventKey="diskUsage">Disk Usage</Nav.Link>
                                <Nav.Link eventKey="threadCount">Thread Count</Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Col>
                    <Col sm={9}>
                        <Tab.Content>
                            <Tab.Pane eventKey="overview">
                                <Overview containerData={containerData} serverData={serverData} timeAgo={timeAgo} isActive={activeTab === "overview"} fetchDiagnostics={fetchDiagnosticsFor} />
                            </Tab.Pane>
                            <Tab.Pane eventKey="running">
                                <RunningView diagnosticsData={containerData.diagnosticsData} timeAgo={timeAgo} isActive={activeTab === "running"} fetchDiagnostics={fetchDiagnosticsFor} />
                            </Tab.Pane>
                            <Tab.Pane eventKey="cpuUsage">
                                <CpuView containerData={containerData} serverData={serverData} timeAgo={timeAgo} isActive={activeTab === "cpuUsage"} fetchDiagnostics={fetchDiagnosticsFor} />
                            </Tab.Pane>
                            <Tab.Pane eventKey="ramUsage">
                                <RamView containerData={containerData} serverData={serverData} timeAgo={timeAgo} isActive={activeTab === "ramUsage"} fetchDiagnostics={fetchDiagnosticsFor} />
                            </Tab.Pane>
                            <Tab.Pane eventKey="diskUsage">
                                <DiskUsageView containerData={containerData} serverData={serverData} timeAgo={timeAgo} isActive={activeTab === "diskUsage"} fetchDiagnostics={fetchDiagnosticsFor} />
                            </Tab.Pane>
                            <Tab.Pane eventKey="threadCount">
                                <ThreadCountView containerData={containerData} serverData={serverData} timeAgo={timeAgo} isActive={activeTab === "threadCount"} fetchDiagnostics={fetchDiagnosticsFor} />
                            </Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container>
        </div>
    );
}
