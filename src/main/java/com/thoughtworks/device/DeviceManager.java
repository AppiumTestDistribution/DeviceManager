package com.thoughtworks.device;

import com.thoughtworks.utils.CommandPromptUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DeviceManager implements Device {

    CommandPromptUtil commandPromptUtil;
    private HashMap<Object, Object> availableSimulators;

    public DeviceManager() {
        commandPromptUtil = new CommandPromptUtil();
        availableSimulators = new HashMap<>();
    }

    public HashMap<Object, Object> getAllSimulators(String OSType)
            throws InterruptedException, IOException {
        Object devices = getAllAvailableSimulators();
        ((LinkedHashMap) devices).forEach((key, value) -> {
            if((OSType).equalsIgnoreCase(((String) key).split(" ")[0])) {
                availableSimulators.put(key,value);
            }
        });
        return availableSimulators;
    }

    public String getSimulatorUDID(String deviceName, String OSVersion, String OSType)
            throws Throwable {
        HashMap<Object, Object> allSimulators = getAllSimulators(OSType);
        Object filerOS = allSimulators.entrySet().stream().filter(objectEntry ->
                (objectEntry.getKey().toString()).contains(OSVersion)).
                findFirst().map(objectObjectEntry -> {
                    return objectObjectEntry.getValue();
                }).orElseThrow(() -> new RuntimeException("Incorrect OS version is provided -- " + OSVersion));

        Object getUDID = ((ArrayList) filerOS).stream().filter(o ->
                ((LinkedHashMap) o).get("name").
                        equals(deviceName)).findFirst().map(o -> {
            return ((LinkedHashMap) o).get("udid");
        }).orElseThrow(() -> new RuntimeException("Incorrect DeviceName is provided -- " + deviceName));
        return (String) getUDID;
    }

    public String getSimulatorState(String simName, String OSVersion, String OSType)
            throws Throwable {
        HashMap<Object, Object> allSimulators = getAllSimulators(OSType);
        Object filerOS = allSimulators.entrySet().stream().filter(objectObjectEntry ->
                objectObjectEntry.getKey().toString().contains(OSVersion))
                .findFirst().map(objectObjectEntry -> {
                    return objectObjectEntry.getValue();
                }).orElseThrow(() -> new RuntimeException("Incorrect OS version is provided -- " + OSVersion));

        Object getState = ((ArrayList) filerOS).stream().filter(o ->
                ((LinkedHashMap) o).get("name").
                        equals(simName)).findFirst().map(o -> {
            return ((LinkedHashMap) o).get("state");
        }).orElseThrow(() -> new RuntimeException("Incorrect DeviceName is provided -- " + simName));
        return (String) getState;
    }

    public void bootSimulator(String deviceName, String OSVersion, String OSType)
            throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, OSVersion, OSType);
        commandPromptUtil.runCommandThruProcess("xcrun simctl boot " + simulatorUDID);
        commandPromptUtil.runCommandThruProcess("open -a Simulator --args -CurrentDeviceUDID "
                + simulatorUDID );
    }

    public void installAppOnSimulator(String deviceName, String OSVersion,
                                      String OSType,String appPath) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, OSVersion, OSType);
        String execute = "xcrun simctl install " + simulatorUDID
                + " " + appPath;
        commandPromptUtil.execForProcessToExecute(execute);
    }

    public void uninstallAppFromSimulator(String deviceName, String OSVersion,
                                      String OSType,String bundleID) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, OSVersion, OSType);
        String execute = "xcrun simctl uninstall " + simulatorUDID
                + " " + bundleID ;
        commandPromptUtil.execForProcessToExecute(execute);
    }

    public void createSimulator(String simName, String OSVersion, String deviceType, String OSType)
            throws Throwable {
        ArrayList<HashMap<Object, Object>> deviceTypes = getAllDeviceTypes();
        Object deviceID = deviceTypes.stream().filter(objectObjectHashMap ->
                objectObjectHashMap.get("name").equals(deviceType)).
                findFirst().map(objectObjectHashMap -> {
            return objectObjectHashMap.get("identifier");
        });
        ArrayList<HashMap<Object, Object>> deviceRuntimes = getAllRuntimes();
        Object deviceRuntime = deviceRuntimes.stream().filter(objectObjectHashMap ->
                objectObjectHashMap.get("name").toString().split(" ")[0].equalsIgnoreCase(OSType)).
                findFirst().map(objectObjectHashMap -> {
            return objectObjectHashMap.get("identifier");
        });
        commandPromptUtil.runCommandThruProcess("xcrun simctl create " + simName + " " + deviceID.toString() + " " +
                deviceRuntime.toString());
    }

    public void deleteSimulator(String simName, String OSVersion, String OSType)
            throws Throwable {
        HashMap<Object, Object> allSimulators = getAllSimulators(OSType);
        Object filerOS = allSimulators.entrySet().stream().filter(objectObjectEntry ->
                objectObjectEntry.getKey().toString().contains(OSVersion))
                .findFirst().map(objectObjectEntry -> {
                    return objectObjectEntry.getValue();
                }).orElseThrow(() -> new RuntimeException("Incorrect OS version is provided -- " + OSVersion));

        Object getUDID = ((ArrayList) filerOS).stream().filter(o ->
                ((LinkedHashMap) o).get("name").
                        equals(simName)).findFirst().map(o -> {
            return ((LinkedHashMap) o).get("udid");
        }).orElseThrow(() -> new RuntimeException("Incorrect DeviceName is provided -- " + simName));
        commandPromptUtil.runCommandThruProcess("xcrun simctl delete " + getUDID.toString());
    }
}
