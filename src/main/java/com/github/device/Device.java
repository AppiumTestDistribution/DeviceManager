package com.github.device;

import lombok.Data;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Data
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
    private boolean isCloud = false;

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
        getOSAndVersion(deviceType);
        this.deviceModel = deviceIdentifier.getOrDefault(this.name, "Not Supported");
        this.deviceManufacturer= "apple";
    }

    private void getOSAndVersion(String deviceType) {
        String[] osAndVersion = deviceType.split(" ");
        if (osAndVersion.length == 2) {
            this.os = osAndVersion[0];
            this.osVersion = osAndVersion[1];
        } else {
            String[] splitDeviceType = deviceType.split("\\.");
            if (splitDeviceType.length == 5) {
                osAndVersion = splitDeviceType[splitDeviceType.length - 1].split("-");
                if (osAndVersion.length == 3) {
                    this.os = osAndVersion[0];
                    this.osVersion = osAndVersion[1] + "." + osAndVersion[2];
                }
            }
        }

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
}
