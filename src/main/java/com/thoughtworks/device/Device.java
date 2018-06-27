package com.thoughtworks.device;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Device {

    private String udid;
    private String name;
    private String state = "Not Supported";
    private boolean isAvailable;
    private String osVersion;
    private String os = "Not Supported";
    private String deviceType = "Not Supported";
    private String brand = "Not Supported";
    private String apiLevel = "Not Supported";
    private boolean isDevice;
    private String deviceModel = "Not Supported";
    private String screenSize;
    private String deviceManufacturer;

    private static Map<String, String> deviceIdentifier = new HashMap<>();

    static {
        deviceIdentifier.put("iPhone 5s", "iPhone6,1");
        deviceIdentifier.put("iPhone 6", "iPhone7,2");
        deviceIdentifier.put("iPhone 6 Plus", "iPhone7,1");
        deviceIdentifier.put("iPhone 6s", "iPhone8,1");
        deviceIdentifier.put("iPhone 6s Plus", "iPhone8,2");
        deviceIdentifier.put("iPhone 7", "iPhone9,1");
        deviceIdentifier.put("iPhone 7 Plus", "iPhone9,2");
        deviceIdentifier.put("iPhone 8", "iPhone10,1");
        deviceIdentifier.put("iPhone 8 Plus", "iPhone10,2");
        deviceIdentifier.put("iPhone SE", "iPhone8,4");
        deviceIdentifier.put("iPhone X", "iPhone10,3");
    }

    public Device(JSONObject deviceJson, String deviceType) {
        this.udid = deviceJson.getString("udid");
        this.name = deviceJson.getString("name");
        this.state = deviceJson.getString("state");
        this.isAvailable = deviceJson.getString("availability").equals("(available)");
        this.deviceType = deviceType;
        String[] osAndVersion = deviceType.split(" ");
        if (osAndVersion.length == 2) {
            this.os = osAndVersion[0];
            this.osVersion = osAndVersion[1];
        }
        this.deviceModel = deviceIdentifier.getOrDefault(this.name, "Not Supported");
    }


    public Device(JSONObject deviceJson) {
        this.udid = deviceJson.getString("udid");
        this.name = deviceJson.getString("name");
        this.osVersion = deviceJson.getString("osVersion");
        this.brand = deviceJson.getString("brand");
        this.apiLevel = deviceJson.getString("apiLevel");
        this.isDevice = deviceJson.getBoolean("isDevice");
        this.deviceModel = deviceJson.getString("deviceModel");
        this.screenSize = deviceJson.getString("screenSize");
        this.os = deviceJson.getString("os");
        this.deviceManufacturer=deviceJson.getString("deviceManufacturer");
    }

    public Device() {
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

    public String getBrand() {
        return brand;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public boolean getIsDevice() {
        return isDevice;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

}
