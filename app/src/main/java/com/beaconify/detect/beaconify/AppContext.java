package com.beaconify.detect.beaconify;

public class AppContext {
    public static final String baseUrl = "http://172.28.119.151/beaconify-api/api";
    public static final String loginEndpoint = "/login.php";
    public static final String beaconsEndpoint = "/beacon.php?beacons={0}&time={1}&day={2}";
    public static final String contentsEndpoint = "/now.php?time={0}&day={1}";
}
