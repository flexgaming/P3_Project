import React, { useEffect, useState } from "react";
import { Table, Stack } from "react-bootstrap";
import TimeRangeDropdown from "./TimeRangeDropdown.jsx";
import "../css/DiagnosticsView.css";
import { defaultTimeFrames } from "../../config/ConfigurationConstants.js";

export default function Overview({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [dataTable, setDataTable] = useState(null);
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.overviewTimeFrame); // Change this for the timeframe of the overview table
    useEffect(() => {
        if (!isActive) return;
        if (typeof fetchDiagnostics === "function") fetchDiagnostics(localTimeFrame);
    }, [isActive, localTimeFrame, fetchDiagnostics]);

    useEffect(() => {
        if (!containerData || !containerData.containerData || !serverData || !serverData.ramTotal) {
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
            rows.push(["CPU Usage:", containerData.containerData && latest.cpuFree != null ? String(containerData.containerData.cpuMax - latest.cpuFree) + "B / " + String(containerData.containerData.cpuMax) + "B" : "N/A"]);
            rows.push(["RAM Usage:", containerData.containerData && latest.ramFree != null ? String(containerData.containerData.ramMax - latest.ramFree) + "B / " + String(containerData.containerData.ramMax) + "B" : "N/A"]);
            rows.push(["Disk Usage:", containerData.containerData && latest.diskUsageFree != null ? String(containerData.containerData.diskUsageMax - latest.diskUsageFree) + "B / " + String(containerData.containerData.diskUsageMax) + "B" : "N/A"]);
            rows.push(["Thread Count:", typeof latest.threadCount !== 'undefined' ? String(latest.threadCount) : "Unknown"]);
        }

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
