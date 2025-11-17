import React, { useEffect, useState } from "react";
import { Tab, Stack, Table, Form, Button, Row, Col, Nav} from "react-bootstrap";
import { Line, Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    LineElement,
    BarElement,
    PointElement,
    LinearScale,
    CategoryScale,
    Tooltip,
    Legend
} from "chart.js";
import pattern from "patternomaly"

ChartJS.register(BarElement, LineElement, PointElement, LinearScale, CategoryScale, Tooltip, Legend);

export default function RunningView({diagnosticsList}) {
    const [runningChart, setRunningChart] = useState(null);

    const timeAgo = (timestamp) => {
        const now = new Date();
        const then = new Date(timestamp);

        const diffMs = now - then;
        const diffSec = Math.floor(diffMs / 1000);
        const diffMin = Math.floor(diffSec / 60);
        const diffHr = Math.floor(diffMin / 60);
        const diffDay = Math.floor(diffHr / 24);

        if (diffSec < 60) return `${diffSec}s ago`;
        if (diffMin < 60) return `${diffMin}m ago`;
        if (diffHr < 24) return `${diffHr}h ago`;
        return `${diffDay}d ago`;
    };

    useEffect(() => {
        diagnosticsList.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

        const labels = diagnosticsList.map(item => timeAgo(item.timestamp));
        const runningValues = diagnosticsList.map(item => (item.running ? 1 : 1));

        const barColors = diagnosticsList.map(item =>
            item.running ? pattern.draw("line", "green") : pattern.draw("diagonal", "red")
        );

        setRunningChart({
//             key: containerID,
            type: "bar",
            data: {
                labels,
                datasets: [
                    {
                        label: "Running",
                        data: runningValues,
                        backgroundColor: barColors,
                        borderColor: barColors,
                        borderWidth: 1,
                        tension: 0.1
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        labels: {
                            generateLabels: function(chart) {
                                return [
                                    {
                                        text: "Running",
                                        fillStyle: pattern.draw("line", "green"),
                                        strokeStyle: "green"
                                    },
                                    {
                                        text: "Stopped",
                                        fillStyle: pattern.draw("diagonal", "red"),
                                        strokeStyle: "red"
                                    }
                                ];
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        min: 0,
                        max: 1,
                        display: false,
                    }
                }
            }
        });
    }, [diagnosticsList]);

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
