import { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import ErrorModal from "./ErrorModal";

function CriticalError() {
    const [errorDetails, setErrorDetails] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchErrorDetails();
    }, []);

    async function fetchErrorDetails() {
        try {
            setLoading(true);
            setError(null);
            const response = await fetch(`/api/data/errors`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const json = await response.json();

            // Extract region objects with both regionID and regionName
            // json structure: { "North America": { regionID: "NA", regionName: "North America" }, ... }
            setErrorDetails(Object.values(json));
        } catch (error) {
            console.error("Error fetching error details:", error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return (
            <tr key="loading">
                <td colSpan="8">Loading errors...</td>
            </tr>
        );
    }

    if (error) {
        return (
            <tr key="error">
                <td colSpan="8">Error: {error}</td>
            </tr>
        );
    }

    if (errorDetails.length === 0) {
        return (
            <tr key="no-errors">
                <td colSpan="8">No critical errors found</td>
            </tr>
        );
    }

    return (
        <>
            {errorDetails.map((error) => (
                <tr key={error.id}>
                    <td>{error.time}</td>
                    <td>{error.date}</td>
                    {/* <td>--DIAGNOSTICS ID--</td> */}
                    <td>
                        {error.regionName} → {error.companyName} → {error.serverID} → {error.containerID}
                    </td>
                    <td>{error.containerName}</td>
                    <td>{error.errorLogs}</td>
                    <td>
                        <ErrorModal error={error /* Alternatively error.errorLogs */} buttonText="View" buttonVariant="primary" />
                    </td>
                    <td>
                        <Button variant="success">Resolve</Button>
                    </td>
                </tr>
            ))}
        </>
    );
}

export default CriticalError;
