import React, { useEffect, useState } from "react";
import "./css/DiagnosticsView.css";
import { Stack, Table, Form, Button } from "react-bootstrap";

export default function DiagnosticsView({ containerID }) {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);

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

                console.log(diagnosticsList);
            } catch (err) {
                setError(err.message || String(err));
            }
        }

        fetchDiagnosticsData();
    })

    return (
        <main className="diagnostics-view-container">
            <h2>
                <b>Diagnostics View</b>
            </h2>
            <p>This is the Diagnostics View page.</p>
        </main>
    );
}
