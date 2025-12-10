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

        
        setNoData(false);
        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const diskUsageContainer = diagnosticsData.map(item => (item.diskUsage / 1000000).toFixed(1));
        const diskUsageServer = diagnosticsData.map(item => (item.systemDiskUsage / 1000000).toFixed(1));

    const lineColorContainer = diagnosticsData.map(() => "blue");
    const lineColorServer = diagnosticsData.map(() => "red");

        setDiskUsageChart({
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "Disk usage of the container resources (MB)",
                        data: diskUsageContainer,
                        backgroundColor: lineColorContainer,
                        borderColor: typeof lineColorServer === "string"
                                       ? lineColorContainer
                                       : lineColorContainer[0],
                        borderWidth: 1.5,
                        tension: 0.1
                    },
                    {
                        label: "Total Disk usage of the server resources (MB)",
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

    
    return (
        <>
            {noData ? (
                
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="disk-usage-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                </div>
            ) : diskUsageChart ? (
                
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
