import React, { useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import "../css/DiagnosticsView.css";

export default function Overview({ containerData, serverData, timeAgo }) {
    const [dataTable, setDataTable] = useState(null);
    const [data, setData] = useState([]);

    function addData(values) {
        setData(prev => [...prev, values]);
    }

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

        setData([]);
        addData(["Server Name:", serverData.serverName]);
        addData(["Container Name:", containerData.containerData.containerName]);
        addData(["Latest Ping:", String(timeAgo(diagnosticsData[lastIndex].timestamp))]);
        addData(["Status:", diagnosticsData[lastIndex].status]);
        addData(["Running:", String(diagnosticsData[lastIndex].running)]);
        addData(["CPU Usage:", String(containerData.containerData.cpuMax - diagnosticsData[lastIndex].cpuFree) + "B / " + String(containerData.containerData.cpuMax) + "B"]);
        addData(["RAM Usage:", String(containerData.containerData.ramMax - diagnosticsData[lastIndex].ramFree) + "B / " + String(containerData.containerData.ramMax) + "B"]);
        addData(["Disk Usage:", String(containerData.containerData.diskUsageMax - diagnosticsData[lastIndex].diskUsageFree) + "B / " + String(containerData.containerData.diskUsageMax) + "B"]);
        addData(["Thread Count:", String(diagnosticsData[lastIndex].threadCount)]);

        setDataTable(
            <>
                <div id="overview-table" className="shadow rounded-4">
                    <Table striped bordered hover >
                        <tbody>
                            {data.map((item, index) => (
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
    }, [containerData, serverData])

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
