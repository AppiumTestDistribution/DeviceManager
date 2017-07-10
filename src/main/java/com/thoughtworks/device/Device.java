package com.thoughtworks.device;

import org.json.JSONObject;

public class Device {

    private final String udid;
    private final String name;
    private final String state;
    private final boolean isAvailable;
    private String osVersion;
    private String os;
    private String deviceType;

    public Device(JSONObject deviceJson, String deviceType) {
        this.udid = deviceJson.getString("udid");
        this.name = deviceJson.getString("name");
        this.state = deviceJson.getString("state");
        this.isAvailable = deviceJson.getString("availability").equals("(available)");
        this.deviceType = deviceType;
        String[] osAndVersion = deviceType.split(" ");
        if(osAndVersion.length == 2){
            this.os = osAndVersion[0];
            this.osVersion = osAndVersion[1];
        }
    }

    public String getUdid() {
        return udid;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getOs() {
        return os;
    }
}
