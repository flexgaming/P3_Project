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
        <h2><b>Dashboard</b></h2>

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