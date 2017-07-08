package com.thoughtworks.device;

import com.thoughtworks.utils.CommandPromptUtil;

import java.io.IOException;
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

    public String getSimulatorState(String simName,String OSVersion, String OSType)
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

    public void bootSimulator(String deviceName, String OSVersion, String OSType )
            throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, OSVersion, OSType);
        commandPromptUtil.runCommandThruProcess("xcrun simctl boot " + simulatorUDID);
        commandPromptUtil.runCommandThruProcess("open -a Simulator --args -CurrentDeviceUDID "
                + simulatorUDID );
    }

}
