import React, { useState } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";


function TimeRangeDropdown({ initial = "Last 10 Minutes", onChange }) {
    const [title, setTitle] = useState(initial);

    const handleSelect = (eventKey) => {
        if (!eventKey) return;
        setTitle(eventKey);
        if (typeof onChange === "function") onChange(eventKey);
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
                <Dropdown.Item eventKey="Last 10 Minutes">Last 10 Minutes</Dropdown.Item>
                <Dropdown.Item eventKey="Last 1 Hour">Last 1 Hour</Dropdown.Item>
                <Dropdown.Item eventKey="Last 6 Hours">Last 6 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="Last 12 Hours">Last 12 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="Last 24 Hours">Last 24 Hours</Dropdown.Item>
                <Dropdown.Item eventKey="Last Week">Last Week</Dropdown.Item>
                <Dropdown.Item eventKey="Last Month">Last Month</Dropdown.Item>
                <Dropdown.Item eventKey="Last Year">Last Year</Dropdown.Item>
            </DropdownButton>
        </>
    );
}

export default TimeRangeDropdown;