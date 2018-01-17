package com.thoughtworks.android;

import com.thoughtworks.device.Device;
import com.thoughtworks.interfaces.Manager;
import com.thoughtworks.utils.CommandPromptUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class AndroidManager implements Manager {

    private CommandPromptUtil cmd;
    private JSONObject adbDevices;
    private static Map<String, Process> processUDIDs = new HashMap<>();
    Process process;

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
                + " shell wm size").split(":")[1].replace("\n", "");

        boolean isDevice = true;
        if (deviceOrEmulator.contains("Genymotion")
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
        adbDevices.put("isDevice", isDevice);
        adbDevices.put("deviceModel", deviceModel);
        adbDevices.put("screenSize", getScreenResolution);
        return adbDevices;
    }


    public List<Device> getDevices() throws Exception {
        List<Device> devices = new ArrayList<>();
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
                    devices.add(new Device(deviceInfo));
                }
            }
        }
        return devices;
    }

    @Override
    public Device getDevice(String udid) throws Exception {
        Optional<Device> device = getDevices().stream().filter(d ->
                udid.equals(d.getUdid())).findFirst();
        return device.orElseThrow(() ->
                new RuntimeException("Provided DeviceUDID " + udid
                        + " is not found on the machine")
        );
    }

    public String startADBLog(String udid, String filePath) throws Exception {
        cmd.execForProcessToExecute("adb -s " + udid + " logcat -b all -c");
        process = cmd.execForProcessToExecute("adb -s " + udid + " logcat > " + filePath);
        processUDIDs.put(udid, process);
        return "Collecting ADB logs for device " + udid + " in file " + filePath;
    }

    public String startADBLogWithPackage(String udid, String packageName, String filePath) throws Exception {
        cmd.execForProcessToExecute("adb -s " + udid + " logcat -b all -c");
        process = cmd.execForProcessToExecute("adb -s " + udid
                + " logcat | grep -F \"`adb shell ps | grep " + packageName + " | cut -c10-15`\"" + " > " + filePath);
        processUDIDs.put(udid, process);
        return "Collecting ADB logs for device " + udid + " for package "+packageName+" in file " + filePath;
    }

    public String stopADBLog(String udid) throws Exception {
        Process p = processUDIDs.get(udid);
        int id = (getPid(p) > 0) ? getPid(p) : 0;
        cmd.runCommandThruProcess("kill -9 " + id);
        return "Stopped collecting ADB logs " + udid;
    }

    public int getPid(Process process) {
        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }
}