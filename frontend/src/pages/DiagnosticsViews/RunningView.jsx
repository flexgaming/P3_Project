import React, { useEffect, useState, useRef } from "react";
import { Bar } from "react-chartjs-2";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import {
    Chart as ChartJS,
    BarElement,
    LinearScale,
    CategoryScale,
    Title,
    Tooltip,
    Legend
} from "chart.js";
import pattern from "patternomaly";
import TimeRangeDropdown from "./TimeRangeDropdown.jsx";
import { defaultTimeFrames } from "../../config/ConfigurationConstants.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);


export default function RunningView({ diagnosticsData, timeAgo, isActive, fetchDiagnostics }) {
    const [runningChart, setRunningChart] = useState(null);
    const [noData, setNoData] = useState(false);
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.runningViewTimeFrame);
    const chartRef = useRef(null);

    
    const [processedDiagnostics, setProcessedDiagnostics] = useState([]);
    const [selectedDiagnostic, setSelectedDiagnostic] = useState(null);
    const [showModal, setShowModal] = useState(false);

    const handleClick = (event)  => {
        const chart = chartRef.current;
        if (!chart) return;

        const points = chart.getElementsAtEventForMode(
            event.nativeEvent,
            'nearest',
            { intersect: true },
            true
        );

        if (points && points.length > 0) {
            const firstPoint = points[0];
            const idx = firstPoint.index;
            const diag = processedDiagnostics[idx];
            if (diag) {
                setSelectedDiagnostic(diag);
                setShowModal(true);
            }
        }
    };

    useEffect(() => {
        if (!isActive) return;
        if (typeof fetchDiagnostics === "function") fetchDiagnostics(localTimeFrame);
    }, [isActive, localTimeFrame, fetchDiagnostics]);

    useEffect(() => {
        if (!diagnosticsData) {
            setProcessedDiagnostics([]);
            return;
        }

        
        let diagArray = [];
        if (diagnosticsData && !Array.isArray(diagnosticsData)) {
            diagArray = Object.entries(diagnosticsData).map(([id, value]) => ({ diagnosticsID: id, ...value }));
        } else if (Array.isArray(diagnosticsData)) {
            
            diagArray = diagnosticsData.slice();
        } else {
            diagArray = [];
        }

        diagArray.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
        
        setProcessedDiagnostics(diagArray);

        
        const labels = diagArray.map(item => timeAgo(item.timestamp));
        const runningValues = diagArray.map(() => -1);

        
        const hasError = diagArray.map(item => {
            const val = item.errorLogs;
            if (Array.isArray(val)) return val.length > 0;
            if (typeof val === "string") return val.trim().length > 0;
            if (val && typeof val === "object") return Object.keys(val).length > 0;
            return false;
        });

        
        
        const errorValues = hasError.map(h => (h ? [0.02, 1] : [0, 0]));

        const barColors = diagArray.map(item =>
            item.running ? pattern.draw("dot", "green") : pattern.draw("cross-dash", "red")
        );
        const errorColors = hasError.map(h => (h ? "yellow" : "rgba(0,0,0,0)"));

        if (diagArray.length === 0) {
            setNoData(true);
            setRunningChart(null);
            return;
        }

        setNoData(false);

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
                        borderWidth: 0,
                        borderSkipped: false,
                        tension: 0.1
                    },
                    {
                        label: "Error",
                        data: errorValues,
                        backgroundColor: errorColors,
                        borderColor: "black",
                        borderWidth: 0,
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
                            generateLabels: function() {
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
                        stacked: true,
                        ticks: {
                            autoSkip: true,
                            maxTicksLimit: 10
                        }
                    },
                    y: {
                        min: -1,
                        max: 1,
                        display: false,
                    }
                }
            }
        });
    }, [diagnosticsData, timeAgo, localTimeFrame, isActive, fetchDiagnostics]);


    
    
    const selectedErrorVal = selectedDiagnostic ? (selectedDiagnostic.errorLogs ?? selectedDiagnostic.errors ?? selectedDiagnostic.error) : null;
    return (
        <>
            {noData ? (
                
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="running-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <div style={{ padding: 20 }}>Error: No data in the selected timeframe.</div>
                </div>
            ) : runningChart ? (
                
                <div className="chart-container shadow rounded-4">
                    <br></br>
                    <TimeRangeDropdown id="running-view-dropdown" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} />
                    <Bar data={runningChart?.data} options={runningChart?.options} ref={chartRef} onClick={handleClick} />

                    {}
                    <Modal show={showModal} onHide={() => setShowModal(false)} size="lg" centered>
                        <Modal.Header closeButton>
                            <Modal.Title>Diagnostic Details</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {selectedDiagnostic ? (
                                <div>
                                    {selectedDiagnostic.diagnosticsID && (
                                        <div style={{ marginBottom: 8 }}><strong>ID:</strong> {selectedDiagnostic.diagnosticsID}</div>
                                    )}

                                    {selectedErrorVal && (
                                        <div style={{ marginBottom: 12 }}>
                                            <h6>Error Logs</h6>
                                            <pre style={{ whiteSpace: "pre-wrap", wordBreak: "break-word", background: "#f8f9fa", padding: 8 }}>{
                                                typeof selectedErrorVal === 'string'
                                                    ? selectedErrorVal
                                                    : JSON.stringify(selectedErrorVal, null, 2)
                                            }</pre>
                                        </div>
                                    )}

                                    <h6>Full diagnostic object</h6>
                                    <pre style={{ whiteSpace: "pre-wrap", wordBreak: "break-word" }}>{JSON.stringify(selectedDiagnostic, null, 2)}</pre>
                                </div>
                            ) : (
                                <div>No diagnostic selected.</div>
                            )}
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={() => setShowModal(false)} style={{ color: "#ffffff" }}>
                                Close
                            </Button>
                        </Modal.Footer>
                    </Modal>
                </div>
            ) : (
                <p>Loading chart…</p>
            )}
        </>
    )
}
