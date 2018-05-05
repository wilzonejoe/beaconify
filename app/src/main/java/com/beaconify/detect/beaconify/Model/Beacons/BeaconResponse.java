package com.beaconify.detect.beaconify.Model.Beacons;

public class BeaconResponse
{
    private String statusCode;

    private Classes[] classes;

    public String getStatusCode ()
    {
        return statusCode;
    }

    public void setStatusCode (String statusCode)
    {
        this.statusCode = statusCode;
    }

    public Classes[] getClassRoom ()
    {
        return classes;
    }

    public void setClass (Classes[] classes)
    {
        this.classes = classes;
    }
}