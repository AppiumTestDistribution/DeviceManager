package com.thoughtworks.android;

import com.thoughtworks.device.Device;
import com.thoughtworks.interfaces.Manager;
import com.thoughtworks.utils.CommandPromptUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AndroidManager implements Manager {

    private CommandPromptUtil cmd;
    private JSONObject adbDevices;

    public AndroidManager() {
        cmd = new CommandPromptUtil();
        adbDevices = new JSONObject();
    }

    /**
     * This method start adb server
     */
    public void startADB() throws Exception {
        String output = cmd.runCommandThruProcess("adb start-server");
        String[] lines = output.split("\n");
        if (lines[0].contains("internal or external command")) {
            System.out.println("Please set ANDROID_HOME in your system variables");
        }
    }

    public JSONObject getDeviceInfo(String deviceID) throws InterruptedException, IOException {
        String model =
                cmd.runCommandThruProcess("adb -s " + deviceID
                        + " shell getprop ro.product.model");
        String brand =
                cmd.runCommandThruProcess("adb -s " + deviceID
                        + " shell getprop ro.product.brand")
                        .replaceAll("\\s+", "");
        String osVersion = cmd.runCommandThruProcess(
                "adb -s " + deviceID + " shell getprop ro.build.version.release")
                .replaceAll("\\s+", "");
        String deviceName = brand + " " + model;
        String apiLevel =
                cmd.runCommandThruProcess("adb -s " + deviceID
                        + " shell getprop ro.build.version.sdk")
                        .replaceAll("\n", "");
        String deviceOrEmulator = cmd.runCommandThruProcess("adb -s " +
                deviceID +
                " shell getprop ro.product.manufacturer");
        String getScreenResolution = cmd.runCommandThruProcess("adb -s " + deviceID
                + " shell wm size").split(":")[1].replace("\n","");

        boolean isDevice = true;
        if(deviceOrEmulator.contains("Genymotion")
                || deviceOrEmulator.contains("unknown")) {
            isDevice = false;
        }

        String deviceModel = cmd.runCommandThruProcess("adb -s " + deviceID
                + " shell getprop ro.product.model");
        adbDevices.put("name", deviceName);
        adbDevices.put("osVersion", osVersion);
        adbDevices.put("apiLevel", apiLevel);
        adbDevices.put("brand", brand);
        adbDevices.put("udid", deviceID);
        adbDevices.put("isDevice",isDevice);
        adbDevices.put("deviceModel",deviceModel);
        adbDevices.put("screenSize",getScreenResolution);
        return adbDevices;
    }


    public List<Device> getDeviceProperties() throws Exception {
        List<Device> device = new ArrayList<>();
        JSONObject adb = new JSONObject();
        startADB(); // start adb service
        String output = cmd.runCommandThruProcess("adb devices");
        String[] lines = output.split("\n");

        if (lines.length <= 1) {
            //stopADB();
        } else {
            for (int i = 1; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s+", "");

                if (lines[i].contains("device")) {
                    lines[i] = lines[i].replaceAll("device", "");
                    String deviceID = lines[i];
                    JSONObject deviceInfo = getDeviceInfo(deviceID);
                    device.add(new Device(deviceInfo));
                }
            }
        }
        return device;
    }

    @Override
    public Device getDevice(String udid) throws Exception {
        Optional<Device> device = getDeviceProperties().stream().filter(d ->
                udid.equals(d.getUdid())).findFirst();
        return device.orElseThrow(() ->
                new RuntimeException("Provided DeviceUDID " + udid
                        + " is not found on the machine")
        );
    }


}
