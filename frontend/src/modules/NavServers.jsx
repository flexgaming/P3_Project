import React, { useEffect, useState } from "react";
import { Alert, Row, Col, Badge, ListGroup, Stack, Image } from "react-bootstrap";
import "../pages/css/Nav.css";
import { IoCloudOfflineOutline } from "react-icons/io5";

function NavServers({ regionID, companyID }) {
    const [servers, setServers] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        let mounted = true;

        async function fetchServers() {
            try {
                const res = await fetch(`/data/${regionID}/${companyID}/contents`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();
                console.log(json);

                const serverList = [];

                if (json && typeof json === "object" && !Array.isArray(json)) {
                    for (const [sKey, sVal] of Object.entries(json)) {
                        if (!sVal || typeof sVal !== "object") continue;

                        const containersObj = sVal.containers && typeof sVal.containers === "object" ? sVal.containers : {};
                        const containers = Object.entries(containersObj).map(([cKey, cVal]) => {
                            return {
                                containerID: cVal?.containerID ?? cKey,
                                containerName: cVal?.containerName ?? cVal?.name ?? cKey,
                                diagnosticsData: Array.isArray(cVal?.diagnosticsData) ? cVal.diagnosticsData : [],
                                raw: cVal,
                            };
                        });

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

                if (mounted) setServers(serverList);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        fetchServers();
        return () => {
            mounted = false;
        };
    }, [regionID, companyID]);

    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );
    
    function chunkArray(arr, size = 3) {
        const out = [];
        for (let i = 0; i < arr.length; i += size) {
            out.push(arr.slice(i, i + size));
        }
        return out;
    }

    const rows = chunkArray(servers, 3);

    return (
        <>
            {rows.map((chunk, rowIndex) => (
                <Row key={`row-${rowIndex}`} className="mb-3">
                    {chunk.map((server) => (
                        <Col className="Server-Column" key={`col-${server.serverID}`}>
                            <ListGroup className="lg1 shadow rounded-4">
                                <ListGroup.Item as="div" disabled className="Server-Header" key={`header-${server.serverID}`}>
                                    <Stack direction="horizontal" gap={2}>
                                        <div>{server.serverName}</div>
                                        <div className="ms-auto">Containers:</div>
                                        <Badge bg="secondary" className="fs-6">{server.containers?.length ?? 0}</Badge>
                                    </Stack>
                                </ListGroup.Item>

                                {server.containers && server.containers.length > 0 ? (
                                    server.containers.map((container) => {
                                        const latest = (container.diagnosticsData && container.diagnosticsData.length > 0)
                                            ? container.diagnosticsData[container.diagnosticsData.length - 1]
                                            : null;
                                        const showWarning = latest?.running;
                                        const uptime = container.diagnosticsData.length > 0
                                            ? ((container.diagnosticsData.filter(d => d.running).length / container.diagnosticsData.length) * 100).toFixed(0)
                                            : "N/A";
                                        const healthVariant =
                                            uptime === "N/A" ? "secondary" :
                                            uptime >= 75 ? "success" :
                                            uptime >= 50 ? "warning" :
                                            "danger";

                                        return (
                                            <ListGroup.Item key={`${container.containerID}-Item`} action href={`/diagnosticsview/${container.containerID}`} variant={healthVariant}>
                                                <Stack direction="horizontal" gap={2}>
                                                    <div className="Container-Name-Container">{container.containerName}</div>
                                                    <div className="ms-auto">
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
