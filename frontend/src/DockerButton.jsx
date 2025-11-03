import React, { useState } from "react";

function DockerButton() {
    const [data, setData] = useState(null);

    const handleClick = async () => {
        try {
            const response = await fetch("/dockers"); // via proxy → http://localhost:8080/dockers
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const jsonData = await response.json();
            console.log(jsonData); // print to console
            setData(jsonData); // store it in state for display (optional)
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "2rem" }}>
            <button onClick={handleClick}>Fetch Dockers</button>

            {data && (
                <pre
                    style={{
                        backgroundColor: "#f4f4f4",
                        padding: "1rem",
                        borderRadius: "8px",
                        textAlign: "left",
                        display: "inline-block",
                        marginTop: "1rem",
                    }}
                >
                    {JSON.stringify(data, null, 2)}
                </pre>
            )}
        </div>
    );
}

export default DockerButton;
