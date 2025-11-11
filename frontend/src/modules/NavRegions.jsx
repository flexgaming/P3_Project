import React, { useEffect, useState } from "react";
import { Spinner, Alert, Tab, Tabs } from "react-bootstrap";

/**
 * NavRegions
 * - Fetches region names and renders them as horizontal tabs at the top of the page.
 * - Shows a spinner while loading and an Alert on error.
 */
export default function NavRegions() {
    const [regions, setRegions] = useState(null);
    const [error, setError] = useState(null);
    const [activeKey, setActiveKey] = useState(null);

    useEffect(() => {
        let mounted = true;
        async function fetchRegions() {
            try {
                const res = await fetch('/api/data/regions');
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json();
                const list = Array.isArray(json) ? json : (json.regions || []);
                if (!mounted) return;
                setRegions(list);
                if (list && list.length > 0) setActiveKey(String(list[0]));
            } catch (err) {
                if (!mounted) return;
                setError(err.message || String(err));
                setRegions([]);
            }
        }
        fetchRegions();
        return () => { mounted = false; };
    }, []);

    if (error) return <div className="p-2"><Alert variant="danger">{error}</Alert></div>;
    if (regions === null) return <div className="p-2"><Spinner animation="border" size="sm" /> Loading regions...</div>;
    if (!regions || regions.length === 0) return <div className="p-2 text-muted">No regions returned by the API.</div>;

    return (
        <Tabs
            id="regions-tabs"
            activeKey={activeKey}
            onSelect={(k) => setActiveKey(k)}
            className="mb-3"
            mountOnEnter
            justify
        >
            {regions.map((name) => (
                <Tab eventKey={String(name)} title={String(name)} key={String(name)}>
                    <div className="p-3">
                        <h3>Companies in {name}</h3>
                        <p className="text-muted">(Region content placeholder)</p>
                    </div>
                </Tab>
            ))}
        </Tabs>
    );
}
