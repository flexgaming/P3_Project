import React, { useState, useEffect } from "react";
import "./css/Dashboard.css";
import { Stack, Table, Form, Button } from "react-bootstrap";
import AddRegionsDashboard from "../modules/DashboardRegions.jsx";
import CriticalError from "../modules/CriticalError.jsx";

export default function Dashboard() {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount
    window.history.replaceState({}, "", `/dashboard/`); // set URL to /dashboard/
    // control the Stack direction responsively using React state
    const [direction, setDirection] = useState(() => (typeof window !== "undefined" && window.innerWidth < 1105) ? "vertical" : "horizontal");

    useEffect(() => {
        function handleResize() {
            setDirection(window.innerWidth < 1105 ? "vertical" : "horizontal");
        }

        window.addEventListener("resize", handleResize);
        // ensure correct direction on mount
        handleResize();
        return () => window.removeEventListener("resize", handleResize);
    }, []);
    
    return (
        <main className="dashboard">
            <h2>
                <b>Dashboard</b>
            </h2>
            <Stack direction={direction} gap={3} id="Region-Cards-Dashboard">
                <AddRegionsDashboard />
            </Stack>
            

            <h1>
                <b>Critical Errors</b>
            </h1>
            <div id="error-table-container" className="shadow">
                <Table striped bordered hover id="error-table" responsive>
                    <thead>
                        <tr>
                            <th>Time</th>
                            <th>Date</th>
                            <th>Container Location</th>
                            <th>Container Name</th>
                            <th>Error Message</th>
                            <th>Error Code</th>
                            <th>Pinned logs</th>
                        </tr>
                    </thead>
                    <tbody>
                        <CriticalError />
                    </tbody>
                </Table>
            </div>
            
        </main>
    );
}
