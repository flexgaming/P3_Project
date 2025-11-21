import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    BarElement,
    LinearScale,
    CategoryScale,
    Tooltip,
    Legend
} from "chart.js";
import pattern from "patternomaly";

ChartJS.register(BarElement, LinearScale, CategoryScale, Tooltip, Legend);

export default function RunningView({ diagnosticsData, timeAgo }) {
    const [runningChart, setRunningChart] = useState(null);

    useEffect(() => {
        if (!diagnosticsData) {
            return;
        }

        diagnosticsData = diagnosticsData && !Array.isArray(diagnosticsData)
            ? Object.values(diagnosticsData)
            : Array.isArray(diagnosticsData)
                ? diagnosticsData
                : [];

        diagnosticsData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        const errors = [1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1];

        const labels = diagnosticsData.map(item => timeAgo(item.timestamp));
        const runningValues = diagnosticsData.map(item => -1);
        const errorValues = errors.map(item => item ? [0.02, 1] : [0, 0]);

        const barColors = diagnosticsData.map(item =>
            item.running ? pattern.draw("dot", "green") : pattern.draw("cross-dash", "red")
        );
        const errorColors = errors.map(item => "yellow");

        setRunningChart({
            type: "bar",
            data: {
                labels,
                datasets: [
                    {
                        label: "Running",
                        data: runningValues,
                        backgroundColor: barColors,
                        borderColor: "black",
                        borderWidth: 3,
                        borderSkipped: false,
                        tension: 0.1
                    },
                    {
                        label: "Error",
                        data: errorValues,
                        backgroundColor: errorColors,
                        borderColor: "black",
                        borderWidth: 3,
                        borderSkipped: false,
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
                            generateLabels: function(chart) {
                                return [
                                    {
                                        text: "Running",
                                        fillStyle: pattern.draw("dot", "green"),
                                        strokeStyle: "black",
                                        lineWidth: 2.5
                                    },
                                    {
                                        text: "Stopped",
                                        fillStyle: pattern.draw("cross-dash", "red"),
                                        strokeStyle: "black",
                                        lineWidth: 2.5
                                    },
                                    {
                                        text: "Error",
                                        fillStyle: "yellow",
                                        strokeStyle: "black",
                                        lineWidth: 2.5
                                    }
                                ];
                            },
                            boxHeight: 20,
                            usePointStyle: false,
                            pointStyle: 'rect',
                            // Custom render function for the boxes
                            render: function(ctx, x, y, legendItem) {
                                const size = legendItem.boxWidth;
                                const lineWidth = legendItem.lineWidth || 1;

                                ctx.save();
                                ctx.fillStyle = legendItem.fillStyle;
                                ctx.strokeStyle = legendItem.strokeStyle || 'black';
                                ctx.lineWidth = lineWidth;

                                ctx.fillRect(x, y, size, size);
                                ctx.strokeRect(x, y, size, size);
                                ctx.restore();
                            }
                        }
                    },
                },
                scales: {
                    x: {
                        stacked: true
                    },
                    y: {
                        min: -1,
                        max: 1,
                        display: false,
                    }
                }
            }
        });
    }, [diagnosticsData]);

    return (
        <>
            <h3>Container running status</h3>

            {runningChart ? (
                <div style={{ width: "95%", height: "50vh", margin: "2.5%" }}>
                    <Bar data={runningChart?.data} options={runningChart?.options}/>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
