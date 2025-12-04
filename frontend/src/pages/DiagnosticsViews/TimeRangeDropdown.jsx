import React, { useEffect, useState } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";
import "../css/Dashboard.css";

// Helpers for converting between readable titles and internal timeframe keys
export function readableTimeFrame(tf) {
    switch (tf) {
        case "10minutes": return "Past 10 Minutes";
        case "1hour": return "Past 1 Hour";
        case "6hours": return "Past 6 Hours";
        case "12hours": return "Past 12 Hours";
        case "24hours": return "Past 24 Hours";
        case "1week": return "Past Week";
        case "1month": return "Past Month";
        case "1year": return "Past Year";
        default:
            return undefined;
    }
}

export function reverseReadableTimeFrame(readable) {
    switch (readable) {
        case "Past 10 Minutes": return "10minutes";
        case "Past 1 Hour": return "1hour";
        case "Past 6 Hours": return "6hours";
        case "Past 12 Hours": return "12hours";
        case "Past 24 Hours": return "24hours";
        case "Past Week": return "1week";
        case "Past Month": return "1month";
        case "Past Year": return "1year";
        default:
            return undefined;
    }
}

// TimeRangeDropdown component
// Props:
// - timeFrame: current selected timeframe (controlled)
// - onChange: function(newTimeFrame) called when the user picks a new value
function TimeRangeDropdown({ timeFrame, onChange, id, className }) {
    const current = timeFrame;
    const [title, setTitle] = useState(current);

    // Keep local title in sync if parent changes timeFrame
    useEffect(() => {
        setTitle(readableTimeFrame(timeFrame));
    }, [timeFrame]);

    // Handle user selection
    const handleSelect = (eventKey) => {
        if (!eventKey) return;
        setTitle(readableTimeFrame(eventKey));
        if (typeof onChange === "function") onChange(eventKey);
    };

    return (
        <>
            <DropdownButton
                className={`${className || "time-interval-dropdown"}`}
                title={title}
                id={id}
                variant="secondary"
                drop="down"
                onSelect={handleSelect}
            >
                <Dropdown.Item eventKey="10minutes">Past 10 Minutes</Dropdown.Item>
                <Dropdown.Item eventKey="1hour">Past 1 Hour</Dropdown.Item>
                <Dropdown.Item eventKey="6hours">Past 6 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="12hours">Past 12 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="24hours">Past 24 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="1week">Past Week</Dropdown.Item>
                <Dropdown.Item eventKey="1month">Past Month</Dropdown.Item>
                <Dropdown.Item eventKey="1year">Past Year</Dropdown.Item>
            </DropdownButton>
        </>
    );
}

export default TimeRangeDropdown;