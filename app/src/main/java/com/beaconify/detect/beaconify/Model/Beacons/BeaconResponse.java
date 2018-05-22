package com.beaconify.detect.beaconify.Model.Beacons;

import java.util.List;

public class BeaconResponse
{
    private String statusCode;

    private List<Classes> classes;

    public String getStatusCode ()
    {
        return statusCode;
    }

    public void setStatusCode (String statusCode)
    {
        this.statusCode = statusCode;
    }

    public List<Classes> getClassRoom ()
    {
        return classes;
    }

    public void setClass (List<Classes> classes)
    {
        this.classes = classes;
    }
}