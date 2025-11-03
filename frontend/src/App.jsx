import { useState } from "react";
import "./App.css";
import { Navbar, Nav, Container, Row, Col, Table, Badge } from "react-bootstrap";

// The main application component
const App = () => {
  // Sample data for the table
  const users = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin', status: 'Active' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'User', status: 'Inactive' },
    { id: 3, name: 'Bob Johnson', email: 'bob@example.com', role: 'Moderator', status: 'Active' },
    { id: 4, name: 'Alice Brown', email: 'alice@example.com', role: 'User', status: 'Active' },
  ];

  const getStatusBadge = (status) => {
    const variant = status === 'Active' ? 'success' : 'secondary';
    return <Badge bg={variant}>{status}</Badge>;
  };

  return (
    <div className="App">
      {/* Navbar */}
      <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
        <Container>
          <Navbar.Brand href="#home">Dashboard</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="#home">Home</Nav.Link>
              <Nav.Link href="#users">Users</Nav.Link>
              <Nav.Link href="#analytics">Analytics</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      {/* Main Content */}
      <Container fluid className="py-4">
        <Row>
          <Col md={12}>
            <h2 className="mb-4">User Management</h2>
            <Table striped bordered hover responsive className="table-dark">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>
                      <Badge bg="info">{user.role}</Badge>
                    </td>
                    <td>{getStatusBadge(user.status)}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default App;
