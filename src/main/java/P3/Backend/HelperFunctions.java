package P3.Backend;


public class HelperFunctions {

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
                return 30 * 24 * 60; 
            case "1year":
                return 365 * 24 * 60; 
            default:
                return Constants.DIAGNOSTICS_TIME_SCOPE;
        }
    }

}