import React, { useEffect, useState } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";

// TimeRangeDropdown component
// Props:
// - initial: default title
// - timeFrame: current selected timeframe (controlled)
// - onChange: function(newTimeFrame) called when the user picks a new value
function TimeRangeDropdown({ timeFrame, onChange }) {
    const current = timeFrame;
    const [title = "Last 10 Minutes", setTitle] = useState(current);

    function translateTimeFrame(tf) {
        switch (tf) {
            case "10minutes":
                return "Last 10 Minutes";
            case "1hour":
                return "Last 1 Hour";
            case "6hours":
                return "Last 6 Hours";
            case "12hours":
                return "Last 12 Hours";
            case "24hours":
                return "Last 24 Hours";
            case "1week":
                return "Last Week";
            case "1month":
                return "Last Month";
            case "1year":
                return "Last Year";
            default:
                break;
        }
    }

    // Keep local title in sync if parent changes timeFrame
    useEffect(() => {
        setTitle(translateTimeFrame(timeFrame));
    }, [timeFrame]);

    const handleSelect = (eventKey) => {
        if (!eventKey) return;
        setTitle(translateTimeFrame(eventKey));
        if (typeof onChange === "function") onChange(eventKey);

        // Broadcast a global timeframe change so other components (including
        // the DiagnosticsView) can react even if they use their own dropdown.
        try {
            const ev = new CustomEvent("p3:timeFrameChanged", { detail: eventKey });
            window.dispatchEvent(ev);
        } catch (err) {
            console.debug("TimeRangeDropdown: falling back to older CustomEvent API", err);
            const ev = document.createEvent("CustomEvent");
            ev.initCustomEvent("p3:timeFrameChanged", true, true, eventKey);
            window.dispatchEvent(ev);
        }
    };

    return (
        <>
            <br></br>
            <DropdownButton
                className="time-interval-dropdown"
                title={title}
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