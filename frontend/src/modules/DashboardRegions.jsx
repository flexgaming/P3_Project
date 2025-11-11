import React, { useEffect, useState } from "react";
import { 
    ListGroup,
    Badge,
    Spinner,
    Alert,
} from "react-bootstrap";

/**
 * AddRegions component
 * - Fetches region names from /api/data/regions on mount
 * - Renders one ListGroup block per region name
 */
function AddRegions() {
    const [regions, setRegions] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        let mounted = true;
        async function fetchRegions() {
            try {
                const res = await fetch('/api/data/regions');
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();
                // Accept either an array or an object { regions: [...] }
                const list = Array.isArray(json) ? json : (json.regions || []);
                if (mounted) setRegions(list);
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            } 
        }

        fetchRegions();
        return () => { mounted = false; };
    }, []);

    if (error) return <div className="p-2"><Alert variant="danger">{error}</Alert></div>;

    return (
        <>
            {regions.map((name, idx) => (
                <div className="p-2 w-100" id={`region-${String(name).replace(/\s+/g, '-').toLowerCase()}`} key={idx}>
                    <ListGroup>
                        <ListGroup.Item><h3><b>{name}</b></h3></ListGroup.Item>
                        <ListGroup.Item>Active Containers: <Badge bg="primary">bitch</Badge> / <Badge bg="primary">—</Badge></ListGroup.Item>
                        <ListGroup.Item>Companies: <Badge bg="primary">—</Badge></ListGroup.Item>
                        <ListGroup.Item>Total uptime: <Badge bg="primary">—</Badge></ListGroup.Item>
                        <ListGroup.Item>Errors last hour: <Badge bg="primary">—</Badge></ListGroup.Item>
                    </ListGroup>
                </div>
            ))}
        </>
    );
}

export default AddRegions;
