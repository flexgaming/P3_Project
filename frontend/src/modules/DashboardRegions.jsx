import React, { useEffect, useState } from "react";
import { ListGroup, Badge, Alert } from "react-bootstrap";
import "../pages/css/Dashboard.css";

/**
 * AddRegions component
 * - Fetches region names from /api/data/dashboard on mount
 * - Renders one ListGroup block per region name
 */
function DashboardRegions() {
    // dashboardData should be an array so we can call .map() when rendering.
    // The backend may return an object keyed by UUIDs; convert that to an array when fetched.
    const [dashboardData, setDashboardData] = useState([]);
    const [error, setError] = useState(null);

    // Fetch regions and dashboard data on mount
    useEffect(() => {
        let mounted = true;

        // Fetch regions and dashboard data on mount
        async function fetchDashboardData() {
            try {
                const res = await fetch("/api/data/dashboard");
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();

                // Normalize the backend response to an array. If the backend already
                // returns an array, use it directly; otherwise convert the object
                // (map keyed by UUID) to an array of values so .map() works below.
                const dataArray = Array.isArray(json) ? json : Object.values(json || {});

                if (mounted) setDashboardData(dataArray);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        // Fetch regions and dashboard data on mount
        fetchDashboardData();
        return () => {
            mounted = false;
        };
    }, []);

    // Render error if fetch failed
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        <>
            {/* Render ListGroup of regions and dashboard data fetched from the backend */}
            {dashboardData.map((currentDashboardData) => {
                // Determine health badge variant based on uptime
                const healthBg =
                    parseFloat(currentDashboardData.uptime) === "N/A" ? "secondary" :
                    parseFloat(currentDashboardData.uptime) >= 75 ? "success" :
                    parseFloat(currentDashboardData.uptime) >= 50 ? "warning" :
                    "danger";
                // If the bg color is yellow (warning), use dark text for readability
                const textColor = (healthBg === "warning") ? "dark" : "light";
                // Render active containers in two different badges: active / total
                const activeContainersParts = currentDashboardData.activeContainers.split("/");
                const activeContainers = activeContainersParts[0] || "N/A";
                const totalContainers = activeContainersParts[1] || "N/A";
                // Change color of active containers badge based on percentage
                const runningPercentage = (totalContainers !== "N/A" && parseInt(totalContainers) > 0)
                    ? (parseInt(activeContainers) / parseInt(totalContainers)) * 100
                    : 0;
                const runningBg =
                    runningPercentage >= 75 ? "success" :
                    runningPercentage >= 50 ? "warning" :
                    "danger";
                return (
                    // Each region card
                    <div className="region-cards p-2 w-100" id={`${currentDashboardData.regionID}`} key={currentDashboardData.regionID}>
                        <ListGroup className="shadow rounded-4">
                            <ListGroup.Item>
                                <h4><b>{currentDashboardData.regionName}</b></h4>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Active Containers: <Badge bg={runningBg}>{activeContainers}</Badge> / <Badge bg={runningBg}>{totalContainers}</Badge>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Companies: <Badge bg="secondary" >{currentDashboardData.companies}</Badge>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Total uptime: <Badge bg={healthBg} text={textColor}>{currentDashboardData.uptime}</Badge>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Errors last hour: <Badge bg="secondary">{currentDashboardData.errors}</Badge>
                            </ListGroup.Item>
                        </ListGroup>
                    </div>
                );
            })}
        </>
    );
}

export default DashboardRegions;
