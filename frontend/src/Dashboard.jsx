import React, { useEffect, useState } from 'react';
import './Dashboard.css';
import {
    Navbar,
    Nav,
    Container,
    Row,
    Col,
    Table,
    Badge,
    Accordion,
    Tab,
    Tabs,
    ListGroup,
    Stack,
    Spinner,
    Alert,
    Image,
    FormCheck,
    Form,
    Button,
} from "react-bootstrap";
import FormCheckLabel from 'react-bootstrap/esm/FormCheckLabel';

/**
 * Class representing a Region item to be injected into the DOM.
 * Creates a small block containing a Bootstrap ListGroup markup so it
 * looks consistent with the other region overviews.
 */
class RegionItem {
    /**
     * @param {string} name - region display name
     */
    constructor(name) {
        this.name = name;
        this.element = this.createRegionItem();
    }

    createRegionItem() {
        // container matches existing layout (.p-2 and width control)
        const container = document.createElement('div');
        container.className = 'p-2 ms-auto w-100 region-item';
        container.id = `region-${this.name.replace(/\s+/g, '-').toLowerCase()}`;

        // create a list-group to mimic React-Bootstrap ListGroup
        const listGroup = document.createElement('div');
        listGroup.className = 'list-group';

        const title = document.createElement('div');
        title.className = 'list-group-item active';
        title.innerHTML = `<h3><b>${this.name}</b></h3>`;

        // placeholder stats (empty by default, can be filled later)
        const stats1 = document.createElement('div');
        stats1.className = 'list-group-item';
        stats1.textContent = 'Active Containers: — / —';

        const stats2 = document.createElement('div');
        stats2.className = 'list-group-item';
        stats2.textContent = 'Companies: —';

        const stats3 = document.createElement('div');
        stats3.className = 'list-group-item';
        stats3.textContent = 'Total uptime: —';

        const stats4 = document.createElement('div');
        stats4.className = 'list-group-item';
        stats4.textContent = 'Errors last hour: —';

        listGroup.appendChild(title);
        listGroup.appendChild(stats1);
        listGroup.appendChild(stats2);
        listGroup.appendChild(stats3);
        listGroup.appendChild(stats4);

        container.appendChild(listGroup);
        return container;
    }
}

/** Append a single region into the #test-Regions stack. */
function regionAdd(regionName) {
    const root = document.getElementById('test-Regions');
    if (!root) {
        console.warn('regionAdd: #test-Regions not found');
        return null;
    }
    const item = new RegionItem(regionName);
    root.appendChild(item.element);
    return item.element;
}

/** Append an array of region names into the #test-Regions stack. Clears existing children. */
function addRegions(regionNames) {
    const root = document.getElementById('test-Regions');
    if (!root) {
        console.warn('addRegions: #test-Regions not found');
        return;
    }
    // clear previous content so we can re-populate cleanly
    while (root.firstChild) root.removeChild(root.firstChild);

    if (!Array.isArray(regionNames)) return;
    for (const name of regionNames) {
        regionAdd(name);
    }
}

// expose helper to window for manual population from browser console or other scripts
window.regionAdd = regionAdd;
window.addRegions = addRegions;

export default function Dashboard() {

    const [regions, setRegions] = useState([]);
    const [loadingRegions, setLoadingRegions] = useState(false);
    const [regionsError, setRegionsError] = useState(null);

    const fetchRegions = async () => {
        // Fetch region list from the backend API
        setLoadingRegions(true);
        setRegionsError(null);
        try {
            const res = await fetch('/api/data/regions');
            if (!res.ok) {
                throw new Error(`Failed to fetch regions: ${res.status} ${res.statusText}`);
            }
            const data = await res.json();

            // Accept either an array response or an object with a `regions` field
            const list = Array.isArray(data) ? data : (data.regions || []);
            setRegions(list);
        } catch (err) {
            console.error('fetchRegions error', err);
            setRegionsError(err.message || String(err));
        } finally {
            setLoadingRegions(false);
        }
    };

    useEffect(() => {
        // fetch on mount
        fetchRegions();
    }, []);

    // When regions state updates, populate the test-Regions stack with names
    // (this also prevents lint warnings about unused variables). If you want
    // manual control, remove this effect and call `addRegions(...)` yourself.
    useEffect(() => {
        if (Array.isArray(regions) && regions.length > 0) {
            // assume each region item may be a string or object with `name`
            const names = regions.map(r => (typeof r === 'string' ? r : (r.name || r.region || 'Unnamed')));
            addRegions(names);
        }
    }, [regions]);

    return (
        <main className="dashboard">
            <h2><b>Dashboard</b></h2>
            <Stack direction="horizontal" gap={3} id="test-Regions">
                
            </Stack>
            <Stack direction="horizontal" id="region-overview-stack" gap={3}>
                <div className="p-2 w-100">
                    
                    <ListGroup>
                        <ListGroup.Item><h3><b>Europe</b></h3></ListGroup.Item>
                        <ListGroup.Item>Active Containers: <Badge bg="primary">12873</Badge> / <Badge bg="primary">14154</Badge></ListGroup.Item>
                        <ListGroup.Item>Companies: <Badge bg="primary">56</Badge></ListGroup.Item>
                        <ListGroup.Item>Total uptime: <Badge bg="primary">70%</Badge></ListGroup.Item>
                        <ListGroup.Item>Errors last hour: <Badge bg="primary">123</Badge></ListGroup.Item>
                    </ListGroup>
                </div>
                <div className="p-2 ms-auto w-100">
                    
                    <ListGroup>
                        <ListGroup.Item><h3><b>North America</b></h3></ListGroup.Item>
                        <ListGroup.Item>Active Containers: <Badge bg="primary">21912</Badge> / <Badge bg="primary">23547</Badge></ListGroup.Item>
                        <ListGroup.Item>Companies: <Badge bg="primary">153</Badge></ListGroup.Item>
                        <ListGroup.Item>Total uptime: <Badge bg="primary">85%</Badge></ListGroup.Item>
                        <ListGroup.Item>Errors last hour: <Badge bg="primary">352</Badge></ListGroup.Item>
                    </ListGroup>
                </div>
                <div className="p-2 ms-auto w-100">
                    <ListGroup>
                        <ListGroup.Item><h3><b>Australia</b></h3></ListGroup.Item>
                        <ListGroup.Item>Active Containers: <Badge bg="primary">10543</Badge> / <Badge bg="primary">11672</Badge></ListGroup.Item>
                        <ListGroup.Item>Companies: <Badge bg="primary">56</Badge></ListGroup.Item>
                        <ListGroup.Item>Total uptime: <Badge bg="primary">42%</Badge></ListGroup.Item>
                        <ListGroup.Item>Errors last hour: <Badge bg="primary">251</Badge></ListGroup.Item>
                    </ListGroup>
                </div>
                <div className="p-2 ms-auto w-100">
                    <ListGroup>
                        <ListGroup.Item><h3><b>Asia</b></h3></ListGroup.Item>
                        <ListGroup.Item>Active Containers: <Badge bg="primary">512</Badge> / <Badge bg="primary">754</Badge></ListGroup.Item>
                        <ListGroup.Item>Companies: <Badge bg="primary">12</Badge></ListGroup.Item>
                        <ListGroup.Item>Total uptime: <Badge bg="primary">89%</Badge></ListGroup.Item>
                        <ListGroup.Item>Errors last hour: <Badge bg="primary">42</Badge></ListGroup.Item>
                    </ListGroup>
                </div>
            </Stack>

            <h1><b>Critical Errors</b></h1>
            <Table striped bordered hover id="errors-table">
                <thead>
                    <tr>
                        <th>Time</th>
                        <th>Date</th>
                        <th>Diagnostics ID</th>
                        <th>Container Path</th>
                        <th>Container Name</th>
                        <th>Error Code</th>
                        <th>Error Message</th>
                        <th>
                            <Stack direction="horizontal"> 
                                <div>Resolved?</div>
                                <div className='ms-auto'>
                                    <Form>
                                        <Form.Check 
                                            type="switch"
                                            id="show-resolved-switch"
                                            label="Show Resolved?"
                                        />
                                    </Form>
                                </div>
                            </Stack>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>10:06</td>
                        <td>14/11/2025</td>
                        <td>245</td>
                        <td>Europe/CEGO/Server2</td>
                        <td>TestCont6</td>
                        <td>5534</td>
                        <td><Button variant="primary">View</Button></td>
                        <td><Button variant='success'>Resolve</Button></td>
                    </tr>
                    <tr>
                        <td>10:54</td>
                        <td>14/11/2025</td>
                        <td>274</td>
                        <td>NorthAmerica/Tesla/Giga</td>      
                        <td>Gigapress4</td>
                        <td>512121</td>
                        <td><Button variant='primary'>View</Button></td>
                        <td><Button variant="success">Resolve</Button></td>
                    </tr>
                </tbody>
            </Table>
        </main>
    );
}