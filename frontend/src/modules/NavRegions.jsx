import React, { useEffect, useState } from "react";
import { ListGroup, Badge, Spinner, Alert, Tabs, Tab} from "react-bootstrap";
import NavCompanies from "./NavCompanies.jsx";
import "../pages/css/Nav.css";
import { getRegions } from "../utils/FetchRegions.js";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

/*
 * NavRegions
 * -----------------
 * Fetches available regions from the backend and renders them as a
 * set of React-Bootstrap Tabs. Each Tab contains a `NavCompanies` component
 * which is responsible for rendering companies/servers/containers for that region.
 *
 * Important behavior:
 * - The component supports multiple JSON shapes from the API (map or array).
 * - We track `mounted` during async fetch to avoid updating state after unmount.
 */
function NavRegions() {
    // regions: array of region objects ({ regionID, regionName, ... })
    const [regions, setRegions] = useState([]);
    // activeKey: currently selected tab eventKey (string)
    const [activeKey, setActiveKey] = useState(null);
    // error: stores fetch error message to show to the user
    const [error, setError] = useState(null);
    // Read route params so URLs like /nav/:regionName/:companyName work
    const { regionName: routeRegionName, companyName: routeCompanyName } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        // Use the shared getRegions() helper to fetch and normalize region data.
        let mounted = true;

        async function loadRegions() {
            try {
                const regionList = await getRegions();
                if (mounted) {
                    setRegions(regionList);
                    // If a regionName is provided in the URL, try to select that region.
                    if (routeRegionName) {
                        const requested = String(routeRegionName).toLowerCase();
                        // Try to match by normalized regionName (case-insensitive, spaces -> dashes)
                        const match = regionList.find(r => {
                            const name = String(r.regionName || "").toLowerCase();
                            const slug = name.replace(/\s+/g, "-");
                            return name === requested || slug === requested;
                        });
                        setActiveKey(match ? String(match.regionID) : (regionList.length ? String(regionList[0].regionID) : null));
                    } else {
                        setActiveKey(regionList.length ? String(regionList[0].regionID) : null);
                    }
                }
            } catch (err) {
                if (mounted) setError(err.message || String(err));
            }
        }

        loadRegions();
        return () => { mounted = false; };
    }, [routeRegionName]); // re-run if route param changes

    // If there was a fetching error show an Alert and don't render the tabs
    if (error)
        return (
            <div className="p-2">
                <Alert variant="danger">{error}</Alert>
            </div>
        );

    return (
        <Tabs
            id="regions-tabs"
            activeKey={activeKey}
            // update activeKey when a tab is selected and push the route
            onSelect={(k) => {
                setActiveKey(k);
                const region = regions.find(r => String(r.regionID) === String(k));
                if (region) {
                    const slug = String(region.regionName || "").toLowerCase().replace(/\s+/g, "-");
                    navigate(`/nav/${slug}`);
                }
            }}
            className="mb-3"
            mountOnEnter // only mount tab content when selected
            justify // make tabs stretch to full width
        >
            {/* Render a Tab for each region. Use regionID as the eventKey/key. */}
            {regions.map((region) => (
                <Tab
                    eventKey={String(region.regionID)} // unique key used by Tabs
                    title={String(region.regionName)} // visible tab title
                    key={String(region.regionID)}
                >
                    <div className="p-3">
                        {/* Each Tab renders a NavCompanies component for that region */}
                        <NavCompanies regionID={region.regionID} regionName={region.regionName} selectedCompanyName={routeCompanyName} />
                    </div>
                </Tab>
            ))}
        </Tabs>
    );
}

export default NavRegions;
