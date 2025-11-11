import React from 'react';
import './Dashboard.css';
import { Stack, Table, Form, Button } from "react-bootstrap";
import AddRegionsDashboard from './modules/DashboardRegions.jsx';

export default function Dashboard() {

    // Regions are handled by the AddRegionsDashboard component which fetches on mount

    return (
        <main className="dashboard">
            <h2><b>Dashboard</b></h2>
            <Stack direction="horizontal" gap={3} id="test-Regions">
                <AddRegionsDashboard />
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