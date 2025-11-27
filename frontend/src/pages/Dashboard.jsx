import React, { useState, useEffect } from "react";
import "./css/Dashboard.css";
import { Stack, Table, Form, Button } from "react-bootstrap";
import DashboardRegions from "../modules/DashboardRegions.jsx";
import CriticalError from "../modules/CriticalError.jsx";


/* * Dashboard Page
 * -----------------
 * Renders the main Dashboard page including:
 * - DashboardRegions component which fetches and displays region cards
 * - Critical Errors table displaying recent critical errors
 */
export default function Dashboard() {
    // Regions are handled by the DashboardRegions component which fetches on mount
    window.history.replaceState({}, "", `/dashboard/`); // set URL to /dashboard/
    // control the Stack direction responsively using React state
    const [direction, setDirection] = useState(() => (typeof window !== "undefined" && window.innerWidth < 1105) ? "vertical" : "horizontal");

    // If the windows becomes smaller than 500px wide, make the stack direction of Region-Cards-Dashboard vertical
    window.addEventListener("resize", () => {
        if (window.innerWidth < 750) {
            document.getElementById("Region-Cards-Dashboard").direction = "vertical";
            console.log("Window resized to less than 750px wide, changing Region-Cards-Dashboard to vertical");
        } else {
            document.getElementById("Region-Cards-Dashboard").direction = "horizontal";
            console.log("Window resized to more than 750px wide, changing Region-Cards-Dashboard to horizontal");
        }
    });

    // Update direction on window resize
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
            {/* Region Cards Section */}
            <Stack direction={direction} gap={3} id="Region-Cards-Dashboard">
                <DashboardRegions />
            </Stack>
            
            {/* Critical Errors Section */}
            <h1>
                <b>Critical Errors:</b>
            </h1>
            <div id="error-table-container" className="shadow">
                <Table striped bordered hover id="error-table" responsive>
                    {/* Table Headers */}
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
                    {/* Table Body function to fetch from the backend view */}
                    <tbody>
                        <CriticalError />
                    </tbody>
                </Table>
            </div>
            
        </main>
    );
}
