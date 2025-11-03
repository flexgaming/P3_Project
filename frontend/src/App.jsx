import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import { Navbar, Container, Nav, Button } from "react-bootstrap";

// The main application component
function App() {
    const [count, setCount] = useState(0);
    const [count2, setCount2] = useState(0);

    return (
        <>
            <Navbar bg="dark" variant="dark">
                {" "}
                // Navbar with dark theme
                <Container>
                    <Navbar.Brand href="#">P3 Project</Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link href="http://localhost:8080/dashboard/redirect">
                            Dashboard
                        </Nav.Link>
                    </Nav>
                </Container>
            </Navbar>

            <Container className="mt-4">
                <div className="d-flex align-items-center mb-3">
                    <a href="https://vite.dev" target="_blank" rel="noreferrer">
                        <img src={viteLogo} className="logo" alt="Vite logo" />
                    </a>
                    <a
                        href="http://localhost:8080/dashboard/redirect"
                        target="_blank"
                        rel="noreferrer"
                    >
                        <img
                            src={reactLogo}
                            className="logo react"
                            alt="React logo"
                        />
                    </a>
                </div>

                <h1>Vite + localhost:8080/dockers</h1>

                <div className="card p-3">
                    <Button
                        variant="primary"
                        onClick={() => {
                            setCount((count) => count + 1);
                            setCount2((count2) => count2 + 1);
                        }}
                        className="mb-2"
                    >
                        count is {count}
                    </Button>

                    <p>
                        Edit <code>src/App.jsx</code> and save to test HMR
                    </p>

                    <div>
                        <p>
                            Hello World! This is a sample React application
                            using Vite as the build tool.
                        </p>
                        <Button
                            variant="secondary"
                            onClick={() => setCount2((count2) => count2 + 1)}
                        >
                            Buttons clicked {count2} times!
                        </Button>
                    </div>
                </div>

                <p className="read-the-docs mt-3">
                    Click on the Vite and React logos to learn more
                </p>
            </Container>
        </>
    );
}

export default App;
