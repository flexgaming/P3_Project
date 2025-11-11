import React from "react";
import "./Dashboard.css";
import { Stack, Table, Form, Button } from "react-bootstrap";
import AddRegionsDashboard from "./modules/DashboardRegions.jsx";
import CriticalError from "./modules/CriticalError.jsx";

export default function Dashboard() {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount

    return (
        <main className="dashboard">
            <h2>
                <b>Dashboard</b>
            </h2>
            <Stack direction="horizontal" gap={3} id="test-Regions">
                <AddRegionsDashboard />
            </Stack>

            <h1>
                <b>Critical Errors</b>
            </h1>
            <Table striped bordered hover id="errors-table">
                <thead>
                    <tr>
                        <th>Time</th>
                        <th>Date</th>
                        {/* <th>Diagnostics ID</th> */}
                        <th>Container Location</th>
                        <th>Container Name</th>
                        <th>Error Message</th>
                        <th>Error Code</th>
                        <th>
                            <Stack direction="horizontal">
                                <div>Resolved?</div>
                                <div className="ms-auto">
                                    <Form>
                                        <Form.Check type="switch" id="show-resolved-switch" label="Show Resolved?" />
                                    </Form>
                                </div>
                            </Stack>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <CriticalError />
                </tbody>
            </Table>
        </main>
    );
}
