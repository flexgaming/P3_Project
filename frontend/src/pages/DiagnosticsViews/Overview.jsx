import React, { useEffect, useState } from "react";
import { Table, Stack } from "react-bootstrap";
import TimeRangeDropdown from "./TimeRangeDropdown.jsx";
import "../css/DiagnosticsView.css";

export default function Overview({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [dataTable, setDataTable] = useState(null);
    const [localTimeFrame, setLocalTimeFrame] = useState("1month"); // Change this for the timeframe of the overview table

    useEffect(() => {
        if (!isActive) return;
        if (typeof fetchDiagnostics === "function") fetchDiagnostics(localTimeFrame);
    }, [isActive, localTimeFrame, fetchDiagnostics]);

    useEffect(() => {
        if (!containerData || !containerData.containerData || !serverData || !serverData.serverName) {
            return;
        }

        const diagnosticsData = (containerData && containerData.diagnosticsData)
            ? (!Array.isArray(containerData.diagnosticsData) ? Object.values(containerData.diagnosticsData) : containerData.diagnosticsData)
            : [];

        // Ensure we have an array we can safely operate on
        const safeDiagnostics = Array.isArray(diagnosticsData) ? diagnosticsData : [];
        safeDiagnostics.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        const rows = [];
        rows.push(["Server Name:", serverData.serverName]);
        rows.push(["Container Name:", containerData.containerData.containerName]);

        // If there are no diagnostics in the selected timeframe show friendly placeholders
        if (safeDiagnostics.length === 0) {
            rows.push(["Container ID:", "No data in selected timeframe"]);
            rows.push(["Latest Ping:", "No data in selected timeframe"]);
            rows.push(["Status:", "N/A"]);
            rows.push(["Running:", "N/A"]);
            rows.push(["CPU Usage:", "N/A"]);
            rows.push(["RAM Usage:", "N/A"]);
            rows.push(["Disk Usage:", "N/A"]);
            rows.push(["Thread Count:", "N/A"]);
        } else {
            const latest = safeDiagnostics[safeDiagnostics.length - 1] || {};
            const latestPing = latest.timestamp ? String(timeAgo(latest.timestamp)) : "Unknown";
            rows.push(["Container ID:", latest.containerReference]);
            rows.push(["Latest Ping:", latestPing]);
            rows.push(["Status:", latest.status || "Unknown"]);
            rows.push(["Running:", typeof latest.running !== 'undefined' ? String(latest.running) : "Unknown"]);
            rows.push(["CPU Usage:", containerData.containerData && latest.cpuUsage != null ? String(containerData.containerData.cpuUsage) + "B" : "N/A"]);
            rows.push(["RAM Usage:", containerData.containerData && latest.ramUsage != null ? String(containerData.containerData.ramUsage) + "B" : "N/A"]);
            rows.push(["Disk Usage:", containerData.containerData && latest.diskUsage != null ? String(containerData.containerData.diskUsage) + "B" : "N/A"]);
            rows.push(["Thread Count:", typeof latest.threadCount !== 'undefined' ? String(latest.threadCount) : "Unknown"]);
        }

        console.log(rows);

    // update local UI state only via dataTable
        setDataTable(
            <>
                <div id="overview-table" className="shadow rounded-4">
                    <Table striped bordered hover >
                        <tbody>
                            {rows.map((item, index) => (
                                <tr key={"row-" + index}>
                                    <td>{item[0]}</td>
                                    <td>{item[1]}</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </div>
            </>
        );
    }, [containerData, serverData, timeAgo, localTimeFrame])

    return (
        <>
            {dataTable ? (
                dataTable
                ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
