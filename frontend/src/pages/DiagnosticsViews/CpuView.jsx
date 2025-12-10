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

// Register ChartJS components
ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

// Plugin to render dashed lines in legend
const dashedLegendPlugin = {
    id: "dashedLegendPlugin",

    // Modify legend items after chart update
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

        // Draw each legend item
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
 * CpuView
 *
 * Props:
 * - containerData: object containing container diagnostics and container metadata
 * - serverData: object containing server totals (cpuTotal, ramTotal, etc.)
 * - timeAgo: function(timestamp) -> string used to render human-friendly labels
 * - isActive: boolean indicating whether this view is currently visible/active
 * - fetchDiagnostics: function(timeFrameKey) -> triggers a diagnostics fetch for this view
 *
 * Renders a line chart comparing container and server CPU usage. When the view
 * becomes active or the local timeframe changes, it calls `fetchDiagnostics`
 * with the selected timeframe.
 *
 * @param {{containerData: object, serverData: object, timeAgo: function, isActive: boolean, fetchDiagnostics: function}} param0
 * @returns {JSX.Element}
 */
export default function CpuView({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [cpuChart, setCpuChart] = useState(null);
    const [noData, setNoData] = useState(false);
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.CpuViewTimeFrame);

    // When the view becomes active, or when the local timeframe changes while
    // active, request diagnostics for our local timeframe.
    useEffect(() => {
        if (!isActive) return;
        if (typeof fetchDiagnostics === "function") fetchDiagnostics(localTimeFrame);
    }, [isActive, localTimeFrame, fetchDiagnostics]);

    // When containerData or serverData changes, rebuild the chart.
    useEffect(() => {
        if (!containerData || !containerData.containerData || !serverData || !serverData.serverName) {
            return;
        }

        // Get diagnostics data as an array
        const diagnosticsData = containerData.diagnosticsData && !Array.isArray(containerData.diagnosticsData)
            ? Object.values(containerData.diagnosticsData)
            : Array.isArray(containerData.diagnosticsData)
                ? containerData.diagnosticsData
                : [];

        // Sort data by timestamp ascending
        diagnosticsData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        // Check if we have any diagnostics data
        if (diagnosticsData.length === 0) {
            // No diagnostics in selected timeframe — show friendly message instead of chart
            setNoData(true);
            setCpuChart(null);
            return;
        }

        // We have data — ensure noData flag is cleared before building chart
        setNoData(false);
        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const cpuContainer = diagnosticsData.map(item => item.cpuUsage);
        const cpuServer = diagnosticsData.map(item => item.systemCpuUsage);

        // Define line colors for container and server
        const lineColorContainer = diagnosticsData.map(() => "blue");
        const lineColorServer = diagnosticsData.map(() => "red");

        // Build the chart data and options
        setCpuChart({
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "CPU usage of the container resources (%)",
                        data: cpuContainer,
                        backgroundColor: lineColorContainer,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColorContainer
                                       : lineColorContainer[0],
                        borderWidth: 1.5,
                        tension: 0.1,
                    },
                    {
                        label: "Total CPU usage of the server resources (%)",
                        data: cpuServer,
                        backgroundColor: lineColorServer,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColorServer
                                       : lineColorServer[0],
                        borderDash: [5, 5],
                        borderWidth: 1.5,
                        tension: 0.1,
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
                        min: 0,
                        suggestedMax: .01,
                        ticks: {
                            callback: function(values) {
                                return (values * 100).toFixed(0) + "%";
                            }
                        },
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

    // Render the chart, no-data message, or loading state
    return (
        <>
            {noData ? (
                    // No data error message 
                    <div className="chart-container shadow rounded-4">
                        <br></br>
                        <TimeRangeDropdown id="cpu-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                        <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                    </div>
                ) : cpuChart ? (
                    // Chart is ready and data is available
                    <div className="chart-container shadow rounded-4">
                        <br></br>
                        <TimeRangeDropdown id="cpu-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                        <Line data={cpuChart?.data} options={cpuChart?.options} plugins={[dashedLegendPlugin]}/>
                    </div>
                ) : (
                    <p>Loading chart…</p>
                )}
        </>
    )
}

