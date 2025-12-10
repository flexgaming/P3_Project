import React, { useEffect, useState } from "react";
import { ListGroup, Badge, Alert } from "react-bootstrap";
import "../pages/css/Dashboard.css";
import { RegionThresholds } from "../config/ConfigurationConstants.js";


function DashboardRegions() {
    
    
    const [dashboardData, setDashboardData] = useState([]);
    const [error, setError] = useState(null);

    
    useEffect(() => {
        let mounted = true;

        
        async function fetchDashboardData() {
            try {
                const res = await fetch("/data/dashboard");
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();

                
                
                
                const dataArray = Array.isArray(json) ? json : Object.values(json || {});

                if (mounted) setDashboardData(dataArray);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        
        fetchDashboardData();
        return () => {
            mounted = false;
        };
    }, []);

    
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        <>
            {}
            {dashboardData.map((currentDashboardData) => {
                
                const healthBg =
                    parseFloat(currentDashboardData.uptime) === "N/A" ? "secondary" :
                    parseFloat(currentDashboardData.uptime) >= RegionThresholds.uptimeThresholdGreen ? "success" :
                    parseFloat(currentDashboardData.uptime) >= RegionThresholds.uptimeThresholdYellow ? "warning" :
                    "danger";
                
                const textColorHealth = (healthBg === "warning") ? "dark" : "light";
                
                const activeContainersParts = currentDashboardData.activeContainers.split("/");
                const activeContainers = activeContainersParts[0] || "N/A";
                const totalContainers = activeContainersParts[1] || "N/A";
                
                const runningPercentage = (totalContainers !== "N/A" && parseInt(totalContainers) > 0)
                    ? (parseInt(activeContainers) / parseInt(totalContainers)) * 100
                    : 0;
                const runningBg =
                    runningPercentage >= RegionThresholds.activeContainersPercentageThresholdGreen ? "success" :
                    runningPercentage >= RegionThresholds.activeContainersPercentageThresholdYellow ? "warning" :
                    "danger";
                const textColorRunning = (runningBg === "warning") ? "dark" : "light";
                const errorLastHourBg =
                    parseInt(currentDashboardData.errors) === RegionThresholds.errorCountThresholdGreen ? "success" :
                    parseInt(currentDashboardData.errors) <= RegionThresholds.errorCountThresholdYellow ? "warning" :
                    "danger";
                const textColorErrors = (errorLastHourBg === "warning") ? "dark" : "light";
                return (
                    
                    <div className="region-cards p-2 w-100" id={`${currentDashboardData.regionID}`} key={currentDashboardData.regionID}>
                        <ListGroup className="shadow rounded-4">
                            <ListGroup.Item>
                                <h4><b>{currentDashboardData.regionName}</b></h4>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Active containers: 
                                <Badge bg={runningBg} text={textColorRunning} className="fs-6">{activeContainers}</Badge> / 
                                <Badge bg={runningBg} text={textColorRunning} className="fs-6">{totalContainers}</Badge>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Companies: <Badge bg="secondary" className="fs-6">{currentDashboardData.companies}</Badge>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Total uptime: <Badge bg={healthBg} text={textColorHealth} className="fs-6">{currentDashboardData.uptime}</Badge>
                            </ListGroup.Item>
                            <ListGroup.Item>
                                Errors past hour: <Badge bg={errorLastHourBg} text={textColorErrors} className="fs-6">{currentDashboardData.errors}</Badge>
                            </ListGroup.Item>
                        </ListGroup>
                    </div>
                );
            })}
        </>
    );
}

export default DashboardRegions;
