import React from "react";
import "./css/Manage.css";
import { Stack, Table, Form, Button } from "react-bootstrap";

export default function ManagePage() {
    // Regions are handled by the AddRegionsDashboard component which fetches on mount

    return (
        <main className="manage-container">
            <h2>
                <b>Manage Page</b>
            </h2>
            <p>This is the Manage page.</p>
        </main>
    );
}