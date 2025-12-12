
import CpuView from "../pages/DiagnosticsViews/CpuView";
import Overview from "../pages/DiagnosticsViews/Overview";

export const RegionThresholds = {
    activeContainersPercentageThresholdGreen: 100,
    activeContainersPercentageThresholdYellow: 90,

    uptimeThresholdGreen: 75,
    uptimeThresholdYellow: 50,

    errorCountThresholdGreen: 0,
    errorCountThresholdYellow: 5,
};

export const defaultTimeFrames = {
    overviewTimeFrame: "1month",
    runningViewTimeFrame: "10minutes",
    CpuViewTimeFrame: "10minutes",
    ramViewTimeFrame: "10minutes",
    diskUsageViewTimeFrame: "10minutes",
    threadCountViewTimeFrame: "10minutes",
    errorTableTimeFrame: "1hour",
};