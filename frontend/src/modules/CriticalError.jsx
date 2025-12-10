import { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import ErrorModal from "./ErrorModal";
import { BsPin, BsPinAngle } from "react-icons/bs";

function CriticalError({ timeFrame }) {
    const [errorDetails, setErrorDetails] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pinnedIds, setPinnedIds] = useState([]);
    
    useEffect(() => {
        fetchErrorDetails();
        
    }, [timeFrame]);

    async function fetchErrorDetails() {
        try {
            setLoading(true);
            setError(null);
            const response = await fetch(`/data/errors`, {
                method: "POST",
                headers: { "Content-Type": "application/json",},
                body: JSON.stringify({ timeFrame }),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const json = await response.json();
            console.log(json);

            
            
            
            const entries = Object.entries(json || {});
            const list = entries.map(([id, value]) => ({ id, ...value }));

            
            
            
            let saved = [];
            try {
                saved = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
            } catch {
                saved = [];
            }

            const savedIds = Array.isArray(saved) ? saved.map((d) => d.id).filter(Boolean) : saved && typeof saved === "object" ? Object.keys(saved) : [];

            
            const listWithPinned = list.map((item) => ({ ...item, pinned: savedIds.includes(item.id) }));

            
            const extraSaved = Array.isArray(saved) ? saved.filter((s) => !list.some((l) => l.id === s.id)).map((s) => ({ ...s, pinned: true })) : [];

            
            const merged = [...listWithPinned, ...extraSaved];
            merged.sort((a, b) => {
                if ((a.pinned ? 1 : 0) === (b.pinned ? 1 : 0)) return 0;
                return a.pinned ? -1 : 1; 
            });

            setErrorDetails(merged);
        } catch (error) {
            console.error("Error fetching error details:", error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    
    useEffect(() => {
        try {
            const saved = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
            const ids = Array.isArray(saved) ? saved.map((d) => d.id).filter(Boolean) : [];
            setPinnedIds(ids);
        } catch {
            
            setPinnedIds([]);
        }
    }, []);

    
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

    
    function togglePinDiagnostic(error) {
        if (pinnedIds.includes(error.id)) {
            unPinDiagnostic(error.id);
        } else {
            saveDiagnostic(error);
        }
    }

    function saveDiagnostic(error) {
        
        let savedDiagnostics = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
        
        const exists = savedDiagnostics.find((diag) => diag.id === error.id);
        if (!exists) {
            savedDiagnostics.push(error);
            localStorage.setItem("savedDiagnostics", JSON.stringify(savedDiagnostics));
            
            setPinnedIds((prev) => Array.from(new Set([...prev, error.id])));
        } else {
            alert("Diagnostic log already pinned!");
        }
    }

    
    function unPinDiagnostic(errorId) {
        let savedDiagnostics = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
        savedDiagnostics = savedDiagnostics.filter((diag) => diag.id !== errorId);
        localStorage.setItem("savedDiagnostics", JSON.stringify(savedDiagnostics));
        setPinnedIds((prev) => prev.filter((id) => id !== errorId));
    }

    return (
        <>
            {" "}
            {}
            {errorDetails.map((error) => (
                <tr key={error.id}>
                    <td>{error.time}</td>
                    <td>{error.date}</td>
                    <td>
                        <a href={`/diagnosticsview/${error.containerID}`}>
                            <b>
                                <u>
                                    {error.regionName} → {error.companyName} → <br />{error.serverName} → {error.containerName}
                                </u>
                            </b>
                        </a>
                    </td>
                    <td>{error.containerName}</td>
                    <td>{error.errorLogs}</td>
                    <td>
                        <ErrorModal error={error } buttonText="View" buttonVariant="primary" />
                    </td>
                    <td>
                        <Button id={`pin-${error.id}`} variant={pinnedIds.includes(error.id) ? "primary" : "success"} onClick={() => togglePinDiagnostic(error)}>
                            {pinnedIds.includes(error.id) ? <>Pinned <BsPin /></> : <>Pin Log <BsPinAngle /></>}
                        </Button>
                    </td>
                </tr>
            ))}
        </>
    );
}

export default CriticalError;
