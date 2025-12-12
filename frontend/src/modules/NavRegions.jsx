import React, { useEffect, useState } from "react";
import { ListGroup, Badge, Spinner, Alert, Tabs, Tab} from "react-bootstrap";
import NavCompanies from "./NavCompanies.jsx";
import "../pages/css/Nav.css";
import { getRegions } from "../utils/FetchRegions.js";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

function NavRegions() {
    const [regions, setRegions] = useState([]);
    const [activeKey, setActiveKey] = useState(null);
    const [error, setError] = useState(null);
    const { regionName: routeRegionName, companyName: routeCompanyName } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        let mounted = true;

        async function loadRegions() {
            try {
                const regionList = await getRegions();
                if (mounted) {
                    setRegions(regionList);
                    if (routeRegionName) {
                        const requested = String(routeRegionName).toLowerCase();
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
    }, [routeRegionName]); 

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
            onSelect={(k) => {
                setActiveKey(k);
                const region = regions.find(r => String(r.regionID) === String(k));
                if (region) {
                    const slug = String(region.regionName || "").toLowerCase().replace(/\s+/g, "-");
                    navigate(`/nav/${slug}`);
                }
            }}
            className="mb-3"
            mountOnEnter 
            justify 
        >
            {regions.map((region) => (
                <Tab
                    eventKey={String(region.regionID)} 
                    title={String(region.regionName)} 
                    key={String(region.regionID)}
                >
                    <div className="p-3">
                        <NavCompanies regionID={region.regionID} regionName={region.regionName} selectedCompanyName={routeCompanyName} />
                    </div>
                </Tab>
            ))}
        </Tabs>
    );
}

export default NavRegions;
