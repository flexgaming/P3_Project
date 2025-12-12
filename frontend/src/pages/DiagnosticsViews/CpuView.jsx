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

const dashedLegendPlugin = {
    id: "dashedLegendPlugin",

    afterUpdate(chart) {
    const legend = chart.legend;

        legend.legendItems.forEach((item) => {
            const dataset = chart.data.datasets[item.datasetIndex];

            if (!dataset.borderDash) return;

            item.fillStyle = "transparent"; 
            item.strokeStyle = dataset.borderColor;
            item.lineWidth = dataset.borderWidth || 2;

            item.lineDash = dataset.borderDash;
            item.hidden = false;
        });
    },

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

export default function CpuView({ containerData, serverData, timeAgo, isActive, fetchDiagnostics }) {
    const [cpuChart, setCpuChart] = useState(null);
    const [noData, setNoData] = useState(false);
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.CpuViewTimeFrame);

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
            setCpuChart(null);
            return;
        }

        setNoData(false);
        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const cpuContainer = diagnosticsData.map(item => item.cpuUsage);
        const cpuServer = diagnosticsData.map(item => item.systemCpuUsage);

        const lineColorContainer = diagnosticsData.map(() => "blue");
        const lineColorServer = diagnosticsData.map(() => "red");

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

    return (
        <>
            {noData ? (
                    <div className="chart-container shadow rounded-4">
                        <br></br>
                        <TimeRangeDropdown id="cpu-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                        <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                    </div>
                ) : cpuChart ? (
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

