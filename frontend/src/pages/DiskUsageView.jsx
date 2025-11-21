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

export default function DiskUsageView({ containerData, serverData, timeAgo }) {
    const [diskUsageChart, setDiskUsageChart] = useState(null);

    useEffect(() => {
        if (!containerData || !containerData.containerData || !serverData || !serverData.diskUsageTotal) {
            return;
        }

        const diagnosticsData = containerData.diagnosticsData && !Array.isArray(containerData.diagnosticsData)
            ? Object.values(containerData.diagnosticsData)
            : Array.isArray(containerData.diagnosticsData)
                ? containerData.diagnosticsData
                : [];

        diagnosticsData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const diskUsageContainer = diagnosticsData.map(item => 1 - item.diskUsageFree / containerData.containerData.diskUsageMax);
        const diskUsageServer = diagnosticsData.map(item => (containerData.containerData.diskUsageMax - item.diskUsageFree) / serverData.diskUsageTotal);

        const lineColorContainer = diagnosticsData.map(item => "blue");
        const lineColorServer = diagnosticsData.map(item => "red");

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
                        label: "Disk usage of the server resources",
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
    }, [containerData, serverData]);

    return (
        <>
            <h3>Container running status</h3>

            {diskUsageChart ? (
                <div style={{ width: "95%", height: "50vh", margin: "2.5%" }}>
                    <Line data={diskUsageChart?.data} options={diskUsageChart?.options} plugins={[dashedLegendPlugin]}/>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
