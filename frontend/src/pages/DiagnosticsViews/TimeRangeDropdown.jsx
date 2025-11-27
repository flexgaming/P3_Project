import React, { useEffect, useState } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";

// Helpers for converting between readable titles and internal timeframe keys
export function readableTimeFrame(tf) {
    switch (tf) {
        case "10minutes": return "Last 10 Minutes";
        case "1hour": return "Last 1 Hour";
        case "6hours": return "Last 6 Hours";
        case "12hours": return "Last 12 Hours";
        case "24hours": return "Last 24 Hours";
        case "1week": return "Last Week";
        case "1month": return "Last Month";
        case "1year": return "Last Year";
        default:
            return undefined;
    }
}

export function reverseReadableTimeFrame(readable) {
    switch (readable) {
        case "Last 10 Minutes": return "10minutes";
        case "Last 1 Hour": return "1hour";
        case "Last 6 Hours": return "6hours";
        case "Last 12 Hours": return "12hours";
        case "Last 24 Hours": return "24hours";
        case "Last Week": return "1week";
        case "Last Month": return "1month";
        case "Last Year": return "1year";
        default:
            return undefined;
    }
}

// TimeRangeDropdown component
// Props:
// - timeFrame: current selected timeframe (controlled)
// - onChange: function(newTimeFrame) called when the user picks a new value
function TimeRangeDropdown({ timeFrame, onChange, id }) {
    const current = timeFrame;
    const [title = "Last 10 Minutes", setTitle] = useState(current);

    // Keep local title in sync if parent changes timeFrame
    useEffect(() => {
        setTitle(readableTimeFrame(timeFrame));
    }, [timeFrame]);

    const handleSelect = (eventKey) => {
        if (!eventKey) return;
        setTitle(readableTimeFrame(eventKey));
        if (typeof onChange === "function") onChange(eventKey);
        // Do NOT broadcast a global timeframe change; each view should own
        // its own timeframe and call the fetch function provided to it.
    };

    return (
        <>
            <br></br>
            <DropdownButton
                className="time-interval-dropdown"
                title={title}
                id={id}
                variant="secondary"
                drop="start"
                onSelect={handleSelect}
            >
                <Dropdown.Item eventKey="10minutes">Last 10 Minutes</Dropdown.Item>
                <Dropdown.Item eventKey="1hour">Last 1 Hour</Dropdown.Item>
                <Dropdown.Item eventKey="6hours">Last 6 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="12hours">Last 12 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="24hours">Last 24 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="1week">Last Week</Dropdown.Item>
                <Dropdown.Item eventKey="1month">Last Month</Dropdown.Item>
                <Dropdown.Item eventKey="1year">Last Year</Dropdown.Item>
            </DropdownButton>
        </>
    );
}

export default TimeRangeDropdown;