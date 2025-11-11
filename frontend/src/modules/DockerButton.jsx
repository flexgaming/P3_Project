import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";

function DockerButton() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleClick = async () => {
        const containerName = document.getElementById("inputForm.containerName").value;
        try {
            setLoading(true);
            const response = await fetch(`/api/data/container/${containerName}`); // via proxy → http://localhost:8080/API/dockers
            if (!response.ok) {
                setData(null);
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const jsonData = await response.json();
            console.log(jsonData); // print to console
            setData(jsonData); // store it in state for display (optional)
        } catch (error) {
            setError(`Error fetching data: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "2rem" }}>
            <Form>
                <Form.Group className="mb-3" controlId="inputForm.containerName" style={{ maxWidth: "300px", margin: "0 auto" }}>
                    <Form.Control type="text" placeholder="Enter Container Name" />
                </Form.Group>
                <Button variant="secondary" onClick={handleClick} disabled={loading}>
                    Fetch Docker Data
                </Button>
            </Form>
            {data && (
                <pre
                    style={{
                        backgroundColor: "#f4f4f4",
                        padding: "1rem",
                        borderRadius: "8px",
                        textAlign: "left",
                        display: "inline-block",
                        marginTop: "1rem",
                    }}>
                    {JSON.stringify(data, null, 2)}
                </pre>
            )}
        </div>
    );
}

export default DockerButton;
