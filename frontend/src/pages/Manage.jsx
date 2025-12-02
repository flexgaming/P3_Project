import React from "react";
import "./css/Manage.css";
import { Container, Breadcrumb, Tabs, Tab, Accordion, Stack, Table, Form, Button, ListGroup } from "react-bootstrap";
import ManageCompanies from "../modules/ManageCompanies.jsx";
import ManageRegions from "../modules/ManageRegions.jsx";
import { useParams } from "react-router-dom";

export default function ManagePage() {
    const { regionID, companyID } = useParams();
    // Regions are handled by the AddRegionsDashboard component which fetches on mount

    if (companyID) {
        console.log(companyID);
    }
    if (regionID) {
        return <ReturnCompany regionID={regionID}/>;
    }

    return <ReturnRegion/>;
}

function ReturnRegion() {
    return (
        <main className="manage-container">
            <h4>Manage Regions</h4>
            <Container style={{ width: "30% "}}>
                <Form>
                    <Form.Group className="mb-3" controlId="formRegionName">
                    <div className="d-flex gap-2">
                        <Form.Control
                            type="text"
                            placeholder="Name of the region"
                            style={{ width: "60%" }}
                        />
                        <Button>Add Region</Button>
                    </div>
                  </Form.Group>
                </Form>
            </Container>
            <Container className="d-flex justify-content-center gap-2">
                <ManageRegions/>
            </Container>
        </main>
    );
}

function ReturnCompany({ regionID }) {
    return (
        <main>
            <h4>Manage Companies</h4>
            <Container style={{ width: "30% "}}>
                <Form>
                    <Form.Group className="mb-3" controlId="formRegionName">
                    <div className="d-flex gap-2">
                        <Form.Control
                            type="text"
                            placeholder="Name of the company"
                            style={{ width: "60%" }}
                        />
                        <Button>Add Company</Button>
                    </div>
                  </Form.Group>
                </Form>
            </Container>
            <Container className="d-flex justify-content-center gap-2">
                <ManageCompanies regionID={regionID}/>
            </Container>
        </main>
    );
}