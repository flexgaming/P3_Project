import React, { useEffect, useState } from "react";
import { Alert, Row, Col, Badge, ListGroup, Stack, Image } from "react-bootstrap";
import warningSmall from "../assets/Warning128.png";
import "../pages/css/Nav.css";
import { IoCloudOfflineOutline } from "react-icons/io5";

/*
 * NavServers
 * -----------------
 * This component displays a company's servers and their containers as a grid of
 * Bootstrap ListGroup cards. It fetches server/container data from the
 * backend endpoint `/api/data/{regionID}/{companyID}/contents` and normalizes
 * the response into an array of server objects with a `containers` array.
 *
 * Props:
 * - regionID: string (UUID or region key used by the backend)
 * - companyID: string (UUID or company key used by the backend)
 *
 * Data shape (expected):
 * The backend commonly returns a map keyed by server id. Example:
 * {
 *   "srv-101": {
 *     serverID: "srv-101",
 *     serverName: "AetherCore",
 *     containers: {
 *       "ctr-001": { containerID: "ctr-001", containerName: "foo", diagnosticsData: [...] },
 *       ...
 *     }
 *   },
 *   ...
 * }
 *
 * The code below converts that map into an array `serverList` with a normalized
 * shape so the rendering logic is straightforward.
 */
function NavServers({ regionID, companyID }) {
    const [servers, setServers] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Keep track of whether the component is still mounted so we don't set
        // state after unmount (avoids React state update warnings).
        let mounted = true;

        async function fetchServers() {
            try {
                // Fetch server+container data for the given region/company.
                const res = await fetch(`/api/data/${regionID}/${companyID}/contents`);
                // If the response is not OK, throw to be caught below.
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();
                console.log(json);

                // The API returns a map keyed by server id. Convert to an array with normalized fields.
                // Input shape (example):
                // { "srv-101": { serverID: "srv-101", serverName: "AetherCore", containers: { "ctr-001": { ... } } }, ... }
                const serverList = [];

                // Only process objects (not arrays) to handle the common map response.
                if (json && typeof json === "object" && !Array.isArray(json)) {
                    // Iterate over server entries in the returned map
                    for (const [sKey, sVal] of Object.entries(json)) {
                        // Skip invalid server values
                        if (!sVal || typeof sVal !== "object") continue;

                        // Normalize containers to an array even if the API returned an inner map
                        const containersObj = sVal.containers && typeof sVal.containers === "object" ? sVal.containers : {};
                        const containers = Object.entries(containersObj).map(([cKey, cVal]) => {
                            // For each container try to use explicit ids/names, fall back to the map key
                            return {
                                containerID: cVal?.containerID ?? cKey,
                                containerName: cVal?.containerName ?? cVal?.name ?? cKey,
                                diagnosticsData: Array.isArray(cVal?.diagnosticsData) ? cVal.diagnosticsData : [],
                                raw: cVal,
                            };
                        });

                        // Build a normalized server object for rendering
                        serverList.push({
                            serverID: sVal.serverID ?? sKey,
                            serverName: sVal.serverName ?? sVal.serverName ?? sVal.serverName ?? sKey,
                            ramTotal: sVal.ramTotal,
                            cpuTotal: sVal.cpuTotal,
                            diskUsageTotal: sVal.diskUsageTotal,
                            containers,
                            raw: sVal,
                        });
                    }
                }

                // Only update state while still mounted
                if (mounted) setServers(serverList);
            } catch (err) {
                // If an error occurs store it for user feedback (only when mounted)
                if (mounted) setError(err.message || String(err));
            }
        }

        // start fetch and ensure we cancel state updates on unmount
        fetchServers();
        return () => {
            mounted = false;
        };
    }, [regionID, companyID]);

    // If an error occurred during fetch, show an alert
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );
    
    // Utility to chunk an array into smaller arrays of given size
    function chunkArray(arr, size = 3) {
        const out = [];
        for (let i = 0; i < arr.length; i += size) {
            out.push(arr.slice(i, i + size));
        }
        return out;
    }

    // Chunk servers into rows of 3 for grid layout
    const rows = chunkArray(servers, 3);

    return (
        <>
            {rows.map((chunk, rowIndex) => (
                <Row key={`row-${rowIndex}`} className="mb-3">
                    {/* Each server in this row */}
                    {chunk.map((server) => (
                        <Col className="Server-Column" key={`col-${server.serverID}`}>
                            <ListGroup className="lg1 shadow rounded-4">
                                {/* Server header with name and container count */}
                                <ListGroup.Item as="div" disabled className="Server-Header" key={`header-${server.serverID}`}>
                                    <Stack direction="horizontal" gap={2}>
                                        <div>{server.serverName}</div>
                                        <div className="ms-auto">Containers:</div>
                                        <Badge bg="secondary">{server.containers?.length ?? 0}</Badge>
                                    </Stack>
                                </ListGroup.Item>

                                {server.containers && server.containers.length > 0 ? (
                                    // Render each container as a ListGroup.Item
                                    server.containers.map((container) => {
                                        // Get the latest diagnostics sample for this container (if any)
                                        const latest = (container.diagnosticsData && container.diagnosticsData.length > 0)
                                            ? container.diagnosticsData[container.diagnosticsData.length - 1]
                                            : null;
                                        // Show a warning icon when the latest sample indicates the container is not running
                                        const showWarning = latest?.running;
                                        // Compute simple uptime percentage based on available samples
                                        const uptime = container.diagnosticsData.length > 0
                                            ? ((container.diagnosticsData.filter(d => d.running).length / container.diagnosticsData.length) * 100).toFixed(0)
                                            : "N/A";
                                        // Choose a Bootstrap variant by uptime thresholds
                                        const healthVariant =
                                            uptime === "N/A" ? "secondary" :
                                            uptime >= 75 ? "success" :
                                            uptime >= 50 ? "warning" :
                                            "danger";

                                        // Render the container item with name, uptime, and warning icon if applicable
                                        return (
                                            <ListGroup.Item key={`${container.containerID}-Item`} action href={`/diagnosticsview/${container.containerID}`} variant={healthVariant}>
                                                <Stack direction="horizontal" gap={2}>
                                                    <div className="Container-Name-Container">{container.containerName}</div>
                                                    <div className="ms-auto">
                                                        {/* Warning icon if container is not running */}
                                                        <IoCloudOfflineOutline size={28} hidden={showWarning} />
                                                    </div>
                                                    <div className="ms-auto">Uptime:</div>
                                                    <div className="fixed-status" id={`${container.containerID}-Uptime`}>{uptime}%</div>
                                                </Stack>
                                            </ListGroup.Item>
                                        );
                                    })
                                ) : (
                                    <ListGroup.Item key={`no-ctr-${server.serverID}`}>
                                        <div>No containers</div>
                                    </ListGroup.Item>
                                )}
                            </ListGroup>
                        </Col>
                    ))}
                </Row>
            ))}
        </>
    );
}

export default NavServers;
