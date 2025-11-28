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

ChartJS.register(LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

const dashedLegendPlugin = {
    id: "dashedLegendPlugin",

    afterUpdate(chart) {
        const legend = chart.legend;
        const ctx = chart.ctx;

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
 * RamView
 *
 * Props:
 * - containerData: object containing container diagnostics and container metadata
 * - serverData: object containing server totals (ramTotal, cpuTotal, etc.)
 * - timeAgo: function(timestamp) -> string used to render human-friendly labels
 * - isActive: boolean indicating whether this view is currently visible/active
 * - fetchDiagnostics: function(timeFrameKey) -> triggers a diagnostics fetch for this view
 *
 * Renders a line chart comparing container and server RAM usage. When the view
 * becomes active or the local timeframe changes, it calls `fetchDiagnostics`
 * with the selected timeframe.
 *
 * @param {{containerData: object, serverData: object, timeAgo: function, isActive: boolean, fetchDiagnostics: function}} param0
 * @returns {JSX.Element}
 */
export default function RamView({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [ramChart, setRamChart] = useState(null);
    const [noData, setNoData] = useState(false);
    const [localTimeFrame, setLocalTimeFrame] = useState("10minutes");

    useEffect(() => {
        if (!isActive) return;
        if (typeof fetchDiagnostics === "function") fetchDiagnostics(localTimeFrame);
    }, [isActive, localTimeFrame, fetchDiagnostics]);

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

        if (diagnosticsData.length === 0) {
            // No diagnostics in selected timeframe — show friendly message instead of chart
            setNoData(true);
            setRamChart(null);
            return;
        }

        // We have data — ensure noData flag is cleared before building chart
        setNoData(false);
        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const ramContainer = diagnosticsData.map(item => 1 - item.ramFree / containerData.containerData.ramMax);
        const ramServer = diagnosticsData.map(item => (containerData.containerData.ramMax - item.ramFree) / serverData.ramTotal);

    const lineColorContainer = diagnosticsData.map(() => "blue");
    const lineColorServer = diagnosticsData.map(() => "red");

        setRamChart({
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "RAM usage of the container resources",
                        data: ramContainer,
                        backgroundColor: lineColorContainer,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColorContainer
                                       : lineColorContainer[0],
                        borderWidth: 1.5,
                        tension: 0.1
                    },
                    {
                        label: "RAM usage of the server resources",
                        data: ramServer,
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
                    y: {
                        min: 0,
                        max: 1,
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


    
    // Render the component
    return (
        <>
            {noData ? (
                // No data error message
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="ram-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                </div>
            ) : ramChart ? (
                // Chart is ready and data is available
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="ram-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <Line data={ramChart?.data} options={ramChart?.options} plugins={[dashedLegendPlugin]}/>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
