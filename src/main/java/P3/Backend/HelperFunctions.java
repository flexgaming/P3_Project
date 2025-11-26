package P3.Backend;


public class HelperFunctions {

    /**
     * Convert a timeframe label (as sent by the frontend) to minutes.
     * Supported values: 10minutes, 1hour, 6hours, 12hours, 24hours, 1week, 1month, 1year
     * Falls back to Constants.DIAGNOSTICS_TIME_SCOPE when unrecognized.
     */
    public static int getMinutesFromTimeFrame(String timeFrameString) {
        if (timeFrameString == null) return Constants.DIAGNOSTICS_TIME_SCOPE;

        switch (timeFrameString) {
            case "10minutes":
                return 10;
            case "1hour":
                return 60;
            case "6hours":
                return 6 * 60;
            case "12hours":
                return 12 * 60;
            case "24hours":
                return 24 * 60;
            case "1week":
                return 7 * 24 * 60;
            case "1month":
                return 30 * 24 * 60; // approx 30 days
            case "1year":
                return 365 * 24 * 60; // approx 365 days
            default:
                System.out.println("Could not parse timeframe string: " + timeFrameString); // For error logging
                return Constants.DIAGNOSTICS_TIME_SCOPE;
        }
    }

}