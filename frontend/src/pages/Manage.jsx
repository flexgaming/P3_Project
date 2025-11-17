import React from "react";
import "./css/Manage.css";
import { Tabs, Tab, Accordion, Stack, Table, Form, Button, ListGroup } from "react-bootstrap";
import ManageCompanies from "../modules/ManageCompanies.jsx";
import ManageRegions from "../modules/ManageRegions.jsx";

export default function ManagePage() {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount

    return (
        <main className="manage-container">
            <Tabs
            defaultActiveKey="Companies"
            id="manage-tabs"
            className="mb-3"
            >
                <Tab eventKey="Companies" title="Manage Companies">
                    <ManageCompanies />
                    
                </Tab>
                <Tab eventKey="Regions" title="Manage Regions">
                    <ManageRegions />
                </Tab>
            </Tabs>
        </main>
    );
}