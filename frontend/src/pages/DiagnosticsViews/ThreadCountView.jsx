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
import TimeRangeDropdown from "./TimeRangeDropdown.jsx";
import { defaultTimeFrames } from "../../config/ConfigurationConstants.js";

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);


export default function ThreadCountView({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [threadCountChart, setThreadCountChart] = useState(null);
    const [noData, setNoData] = useState(false);
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.threadCountViewTimeFrame);

    useEffect(() => {
        if (!isActive) return;
        if (typeof fetchDiagnostics === "function") fetchDiagnostics(localTimeFrame);
    }, [isActive, localTimeFrame, fetchDiagnostics]);

    useEffect(() => {
        if (!containerData || !containerData.containerData || !serverData || !serverData.serverName) {
            return;
        }

        const diagnosticsData = containerData.diagnosticsData && !Array.isArray(containerData.diagnosticsData)
            ? Object.values(containerData.diagnosticsData)
            : Array.isArray(containerData.diagnosticsData)
                ? containerData.diagnosticsData
                : [];

        diagnosticsData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        if (diagnosticsData.length === 0) {
            setNoData(true);
            setThreadCountChart(null);
            return;
        }

        setNoData(false);

        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const threadCount = diagnosticsData.map(item => item.threadCount);

        const lineColor = diagnosticsData.map(() => "blue");
        setThreadCountChart({
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "Amount of threads running in the Java Virtual Machine",
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
                scales: {
                    x: {
                        ticks: {
                            autoSkip: true,
                            maxTicksLimit: 10
                        }
                    },
                    y: {
                        min: 0
                    }
                },
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
    }, [containerData, serverData, timeAgo, localTimeFrame, isActive, fetchDiagnostics]);

    
    return (
        <>
            {noData ? (
                
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="thread-count-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                </div>
            ) : threadCountChart ? (
                
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="thread-count-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <Line data={threadCountChart?.data} options={threadCountChart?.options}/>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
