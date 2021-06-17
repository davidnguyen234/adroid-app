package com.dlnsoft.adroidapplication;

public class CamItem {
    private final String trafficCamAddress;
    private final double trafficCamLongitude;
    private final double trafficCamLatitude;

    public CamItem(String address, double longitude, double latitude) {
        trafficCamAddress = address;

        trafficCamLongitude = longitude;
        trafficCamLatitude = latitude;

    }

    public String getAddress() {
        return trafficCamAddress;
    }

    public double getLongitude() {
        return trafficCamLongitude;
    }

    public double getLatitude() {
        return trafficCamLatitude;
    }
}
