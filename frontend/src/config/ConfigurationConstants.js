/**
 * ConfigurationConstants.js
 * 
 * 
 */

import CpuView from "../pages/DiagnosticsViews/CpuView";
import Overview from "../pages/DiagnosticsViews/Overview";

export const RegionThresholds = {
    // Active containers percentage thresholds
    activeContainersPercentageThresholdGreen: 100,
    activeContainersPercentageThresholdYellow: 90,

    // Running containers percentage thresholds
    uptimeThresholdGreen: 75,
    uptimeThresholdYellow: 50,

    // Error count thresholds for the past hour
    errorCountThresholdGreen: 0,
    errorCountThresholdYellow: 5,
};

// Default time frames for various diagnostics views
export const defaultTimeFrames = {
    overviewTimeFrame: "1month",
    runningViewTimeFrame: "10minutes",
    CpuViewTimeFrame: "10minutes",
    ramViewTimeFrame: "10minutes",
    diskUsageViewTimeFrame: "10minutes",
    threadCountViewTimeFrame: "10minutes",
    errorTableTimeFrame: "1hour",
};

export const themeColors = {
    appBackground: "#9ED3E8",
    siteBackground: "#5C7C89",
    navBackground: "#011425",
};