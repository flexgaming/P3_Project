import React from "react";
import "./css/Manage.css";
import { Tabs, Tab, Accordion, Stack, Table, Form, Button, ListGroup } from "react-bootstrap";
import ManageCompanies from "../modules/ManageCompanies.jsx";
import ManageRegions from "../modules/ManageRegions.jsx";

export default function ManagePage() {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount

    return (
        <main className="manage-container">
            {/* Manage Page tabs for Companies and Regions */}
            <Tabs
            defaultActiveKey="Companies"
            id="manage-tabs"
            className="mb-3"
            >
                <Tab eventKey="Companies" title="Manage Companies">
                    {/* Manage Companies Section */}
                    <ManageCompanies />
                    
                </Tab>
                <Tab eventKey="Regions" title="Manage Regions">
                    {/* Manage Regions Section */}
                    <ManageRegions />
                </Tab>
            </Tabs>
        </main>
    );
}