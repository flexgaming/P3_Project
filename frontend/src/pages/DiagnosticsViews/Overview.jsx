import React, { useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import "../css/DiagnosticsView.css";

export default function Overview({ containerData, serverData, timeAgo }) {
    const [dataTable, setDataTable] = useState(null);

    useEffect(() => {
        if (!containerData || !containerData.containerData || !serverData || !serverData.ramTotal) {
            return;
        }

        const diagnosticsData = containerData.diagnosticsData && !Array.isArray(containerData.diagnosticsData)
            ? Object.values(containerData.diagnosticsData)
            : Array.isArray(containerData.diagnosticsData)
                ? containerData.diagnosticsData
                : [];

        diagnosticsData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        const lastIndex = diagnosticsData.length - 1;

        const rows = [];
        rows.push(["Server Name:", serverData.serverName]);
        rows.push(["Container Name:", containerData.containerData.containerName]);

        // If there are no diagnostics in the selected timeframe show friendly placeholders
        if (diagnosticsData.length === 0) {
            rows.push(["Latest Ping:", "No data in selected timeframe"]);
            rows.push(["Status:", "N/A"]);
            rows.push(["Running:", "N/A"]);
            rows.push(["CPU Usage:", "N/A"]);
            rows.push(["RAM Usage:", "N/A"]);
            rows.push(["Disk Usage:", "N/A"]);
            rows.push(["Thread Count:", "N/A"]);
        } else {
            const latest = diagnosticsData[lastIndex];
            rows.push(["Latest Ping:", String(timeAgo(latest.timestamp))]);
            rows.push(["Status:", latest.status]);
            rows.push(["Running:", String(latest.running)]);
            rows.push(["CPU Usage:", String(containerData.containerData.cpuMax - latest.cpuFree) + "B / " + String(containerData.containerData.cpuMax) + "B"]);
            rows.push(["RAM Usage:", String(containerData.containerData.ramMax - latest.ramFree) + "B / " + String(containerData.containerData.ramMax) + "B"]);
            rows.push(["Disk Usage:", String(containerData.containerData.diskUsageMax - latest.diskUsageFree) + "B / " + String(containerData.containerData.diskUsageMax) + "B"]);
            rows.push(["Thread Count:", String(latest.threadCount)]);
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
    }, [containerData, serverData, timeAgo])

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
