import React, { useState, useEffect } from "react";
import "./css/Dashboard.css";
import { Stack, Table, Form, Button } from "react-bootstrap";
import DashboardRegions from "../modules/DashboardRegions.jsx";
import CriticalError from "../modules/CriticalError.jsx";
import TimeRangeDropdown from "./DiagnosticsViews/TimeRangeDropdown.jsx";
import { defaultTimeFrames } from "../config/ConfigurationConstants.js";


export default function Dashboard() {
    const [localTimeFrame, setLocalTimeFrame] = useState(defaultTimeFrames.errorTableTimeFrame);
    
    window.history.replaceState({}, "", `/dashboard/`); 
    
    const [direction, setDirection] = useState(() => (typeof window !== "undefined" && window.innerWidth < 1105) ? "vertical" : "horizontal");

    
    window.addEventListener("resize", () => {
        if (window.innerWidth < 750) {
            document.getElementById("Region-Cards-Dashboard").direction = "vertical";
        } else {
            document.getElementById("Region-Cards-Dashboard").direction = "horizontal";
        }
    });

    
    useEffect(() => {
        function handleResize() {
            setDirection(window.innerWidth < 1105 ? "vertical" : "horizontal");
        }

        window.addEventListener("resize", handleResize);
        
        handleResize();
        return () => window.removeEventListener("resize", handleResize);
    }, []);

    return (
        <main className="dashboard">
            <h2>
                <b>Dashboard</b>
            </h2>
            {}
            <Stack direction={direction} gap={0.5} id="Region-Cards-Dashboard">
                <DashboardRegions />
            </Stack>
            
            {}
            <div id="critical-header-container">
                <h1 id="critical-errors-header"><b>Critical Errors:</b></h1>
                <TimeRangeDropdown id="error-dropdown-id" timeFrame={localTimeFrame} onChange={setLocalTimeFrame} className={"error-dropdown shadow"}/>
            </div>
            
            
            <div id="error-table-container" className="shadow">
                <Table striped bordered hover id="error-table" responsive>
                    {}
                    <thead>
                        <tr>
                            <th>Time</th>
                            <th>Date</th>
                            <th>Container Location</th>
                            <th>Container Name</th>
                            <th>Error Severity</th>
                            <th>Error Log</th>
                            <th>Pinned Logs</th>
                        </tr>
                    </thead>
                    {}
                    <tbody>
                        <CriticalError timeFrame={localTimeFrame} />
                    </tbody>
                </Table>
            </div>
        </main>
    );
}
