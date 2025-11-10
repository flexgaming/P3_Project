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

export default function Dashboard() {



  return (
    <main className="dashboard">
        <h2>Dashboard</h2>

        <Stack direction="horizontal" gap={3}>
            <div className="p-2">Europe</div>
            <div className="p-2 ms-auto">North America</div>
            <div className="p-2 ms-auto">Australia</div>
            <div className="p-2 ms-auto">Asia</div>
        </Stack>

        <h1>Critical Errors</h1>
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
                    <th><Stack direction="horizontal"> 
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
                    <td><button variant="warning">View</button></td>
                    <td><button>Resolve</button></td>
                </tr>
                <tr>
                    <td>10:54</td>
                    <td>14/11/2025</td>
                    <td>274</td>
                    <td>NorthAmerica/Tesla/Giga</td>      
                    <td>Gigapress4</td>
                    <td>512121</td>
                    <td><button variant='info'>View</button></td>
                    <td><button>Resolve</button></td>
                </tr>
            </tbody>
        </Table>

    </main>
  );
}