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
import TimeRangeDropdown from "./TimeRangeDropdown.jsx";
import { defaultTimeFrames } from "../../config/ConfigurationConstants.js";

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

// Plugin to render dashed lines in legend
const dashedLegendPlugin = {
    id: "dashedLegendPlugin",

    afterUpdate(chart) {
        const legend = chart.legend;
        // chart.ctx is available if needed for drawing, not required here

        legend.legendItems.forEach((item) => {
            const dataset = chart.data.datasets[item.datasetIndex];

            if (!dataset.borderDash) return;

            // Override the default draw
            item.fillStyle = "transparent"; // hide the filled box
            item.strokeStyle = dataset.borderColor;
            item.lineWidth = dataset.borderWidth || 2;

            item.lineDash = dataset.borderDash;
            item.hidden = false;
        });
    },

    // Actual drawing of the legend item
    afterDraw(chart) {
        const ctx = chart.ctx;
        const legend = chart.legend;

        legend.legendItems.forEach((item) => {
            const dataset = chart.data.datasets[item.datasetIndex];

            if (!dataset.borderDash) return;

            const x = item.textX - 20;
            const y = item.top + item.height / 2;

            ctx.save();
            ctx.setLineDash(dataset.borderDash);
            ctx.strokeStyle = dataset.borderColor[0];
            ctx.lineWidth = dataset.borderWidth || 2;
            ctx.beginPath();
            ctx.moveTo(x, y);
            ctx.lineTo(x + 20, y);
            ctx.stroke();
            ctx.restore();
        });
    },
};

/**
 * DiskUsageView
 *
 * Props:
 * - containerData: object containing container diagnostics and container metadata
 * - serverData: object containing server totals (diskUsageTotal, ramTotal, etc.)
 * - timeAgo: function(timestamp) -> string used to render human-friendly labels
 * - isActive: boolean indicating whether this view is currently visible/active
 * - fetchDiagnostics: function(timeFrameKey) -> triggers a diagnostics fetch for this view
 *
 * Renders a line chart comparing container and server disk usage. When the view
 * becomes active or the local timeframe changes, it calls `fetchDiagnostics`
 * with the selected timeframe.
 *
 * @param {{containerData: object, serverData: object, timeAgo: function, isActive: boolean, fetchDiagnostics: function}} param0
 * @returns {JSX.Element}
 */
export default function DiskUsageView({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [diskUsageChart, setDiskUsageChart] = useState(null);
    const [noData, setNoData] = useState(false);
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.diskUsageViewTimeFrame);

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
            setDiskUsageChart(null);
            return;
        }

        // Data is present — clear the no-data state so chart can render
        setNoData(false);
        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const diskUsageContainer = diagnosticsData.map(item => item.diskUsage / 1000000);
        const diskUsageServer = diagnosticsData.map(item => item.systemDiskUsage / 1000000);

    const lineColorContainer = diagnosticsData.map(() => "blue");
    const lineColorServer = diagnosticsData.map(() => "red");

        setDiskUsageChart({
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "Disk usage of the container resources",
                        data: diskUsageContainer,
                        backgroundColor: lineColorContainer,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColorContainer
                                       : lineColorContainer[0],
                        borderWidth: 1.5,
                        tension: 0.1
                    },
                    {
                        label: "Total Disk usage of the server resources",
                        data: diskUsageServer,
                        backgroundColor: lineColorServer,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColorServer
                                       : lineColorServer[0],
                        borderDash: [5, 5],
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

    // Render the component 
    return (
        <>
            {noData ? (
                // No data error message
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="disk-usage-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                </div>
            ) : diskUsageChart ? (
                // Chart is ready and data is available
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="disk-usage-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <Line data={diskUsageChart?.data} options={diskUsageChart?.options} plugins={[dashedLegendPlugin]}/>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
