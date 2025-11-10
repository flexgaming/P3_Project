import React, { useState } from "react";

function DockerButton() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleClick = async () => {
        try {
            setLoading(true);
            const response = await fetch("/api/data/ctr-011"); // via proxy → http://localhost:8080/API/dockers
            if (!response.ok) {
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
            <div>
                <button onClick={handleClick}>Fetch Dockers</button>
            </div>
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
