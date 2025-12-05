import { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import ErrorModal from "./ErrorModal";
import { BsPin, BsPinAngle } from "react-icons/bs";

function CriticalError({ timeFrame }) {
    const [errorDetails, setErrorDetails] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pinnedIds, setPinnedIds] = useState([]);
    // Fetch error details on mount and whenever the selected timeframe changes
    useEffect(() => {
        fetchErrorDetails();
        // eslint-disable-next-line react-hooks/exhaustive-deps
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

            // The API returns an object keyed by diagnostics UUID, e.g.
            // { "0efd...": { date: ..., errorLogs: ..., ... }, ... }
            // Convert to an array while preserving the UUID as `id` on each entry.
            const entries = Object.entries(json || {});
            const list = entries.map(([id, value]) => ({ id, ...value }));

            // Read saved diagnostics directly from localStorage and compute saved ids.
            // We read localStorage here (synchronously) because React state updates for
            // `localErrors` are asynchronous and would not be available immediately.
            let saved = [];
            try {
                saved = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
            } catch {
                saved = [];
            }

            const savedIds = Array.isArray(saved) ? saved.map((d) => d.id).filter(Boolean) : saved && typeof saved === "object" ? Object.keys(saved) : [];

            // Mark fetched items as pinned when they exist in saved diagnostics
            const listWithPinned = list.map((item) => ({ ...item, pinned: savedIds.includes(item.id) }));

            // Include any saved-only items (not present in fetched list) so they appear after fetched items
            const extraSaved = Array.isArray(saved) ? saved.filter((s) => !list.some((l) => l.id === s.id)).map((s) => ({ ...s, pinned: true })) : [];

            // Merge and sort so that pinned items appear at the top on initial load
            const merged = [...listWithPinned, ...extraSaved];
            merged.sort((a, b) => {
                if ((a.pinned ? 1 : 0) === (b.pinned ? 1 : 0)) return 0;
                return a.pinned ? -1 : 1; // pinned first
            });

            setErrorDetails(merged);
        } catch (error) {
            console.error("Error fetching error details:", error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    // load pinned diagnostics ids from localStorage so we can render button state
    useEffect(() => {
        try {
            const saved = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
            const ids = Array.isArray(saved) ? saved.map((d) => d.id).filter(Boolean) : [];
            setPinnedIds(ids);
        } catch {
            // ignore parse errors and default to empty
            setPinnedIds([]);
        }
    }, []);

    // Render loading, error, or no errors states
    if (loading) {
        return (
            <tr key="loading">
                <td colSpan="8">Loading errors...</td>
            </tr>
        );
    }

    // Render error state
    if (error) {
        return (
            <tr key="error">
                <td colSpan="8">Error: {error}</td>
            </tr>
        );
    }

    // Render no errors state
    if (errorDetails.length === 0) {
        return (
            <tr key="no-errors">
                <td colSpan="8">No critical errors found</td>
            </tr>
        );
    }

    // Handlers to pin/unpin diagnostic logs
    function togglePinDiagnostic(error) {
        if (pinnedIds.includes(error.id)) {
            unPinDiagnostic(error.id);
        } else {
            saveDiagnostic(error);
        }
    }

    function saveDiagnostic(error) {
        // saves the diagnostic log object on localstorage in an array of saved logs
        let savedDiagnostics = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
        // check if the diagnostic log is already saved
        const exists = savedDiagnostics.find((diag) => diag.id === error.id);
        if (!exists) {
            savedDiagnostics.push(error);
            localStorage.setItem("savedDiagnostics", JSON.stringify(savedDiagnostics));
            // update React state so UI updates declaratively
            setPinnedIds((prev) => Array.from(new Set([...prev, error.id])));
        } else {
            alert("Diagnostic log already pinned!");
        }
    }

    // Unpin diagnostic log by removing it from localStorage
    function unPinDiagnostic(errorId) {
        let savedDiagnostics = JSON.parse(localStorage.getItem("savedDiagnostics")) || [];
        savedDiagnostics = savedDiagnostics.filter((diag) => diag.id !== errorId);
        localStorage.setItem("savedDiagnostics", JSON.stringify(savedDiagnostics));
        setPinnedIds((prev) => prev.filter((id) => id !== errorId));
    }

    return (
        <>
            {" "}
            {/* Render each error detail row */}
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
                        <ErrorModal error={error /* Alternatively error.errorLogs */} buttonText="View" buttonVariant="primary" />
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
