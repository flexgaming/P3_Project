import React, { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import {
    Chart as ChartJS,
    LineElement,
    PointElement,
    LinearScale,
    CategoryScale,
    Tooltip,
    Legend
} from "chart.js";
import pattern from "patternomaly";

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

export default function ThreadCountView({ containerData, serverData, timeAgo }) {
    const [threadCountChart, setThreadCountChart] = useState(null);

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

        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const threadCount = diagnosticsData.map(item => item.threadCount);

        const lineColor = diagnosticsData.map(item => "blue");

        setThreadCountChart({
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "Thread count",
                        data: threadCount,
                        backgroundColor: lineColor,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColor
                                       : lineColor[0],
                        borderWidth: 1.5,
                        tension: 0.1
                    }
                ]
            },
            options: {
                animation: false,
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        labels: {
                            usePointStyle: true,
                            pointStyle: "line",
                            pointStyleWidth: 40
                        }
                    }
                }
            }
        });
    }, [containerData, serverData]);

    return (
        <>
            <h3>Thread count</h3>

            {threadCountChart ? (
                <div style={{ width: "95%", height: "50vh", margin: "2.5%" }}>
                    <Line data={threadCountChart?.data} options={threadCountChart?.options}/>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
