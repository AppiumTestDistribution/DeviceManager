package com.thoughtworks.device;

import com.thoughtworks.utils.CommandPromptUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SimulatorManager implements IDeviceManager {

    private CommandPromptUtil commandPromptUtil;

    public SimulatorManager() {
        commandPromptUtil = new CommandPromptUtil();
    }

    @Override
    public List<Device> getAllSimulators(String osType)
            throws InterruptedException, IOException {
        List<Device> devices = getAllAvailableSimulators();
        List<Device> deviceListForOS = devices.stream().filter(device -> osType.equals(device.getOs())).collect(toList());
        return deviceListForOS;
    }

    @Override
    public Device getDevice(String deviceName, String osVersion, String osType) throws InterruptedException, IOException {
        List<Device> allSimulators = getAllSimulators(osType);
        Optional<Device> device = allSimulators.stream().filter(d ->
                deviceName.equals(d.getName()) &&
                        osVersion.equals(d.getOsVersion()) &&
                        osType.equals(d.getOs())).findFirst();
        return device.orElseThrow(()->
            new RuntimeException("Device Not found with deviceName-"+deviceName+ " osVersion-"+osVersion+" osType-"+osType)
        );
    }

    @Override
    public String getSimulatorUDID(String deviceName, String osVersion, String osType)
            throws Throwable {
        Device device = getDevice(deviceName, osVersion, osType);
        return device.getUdid();
    }

    @Override
    public String getSimulatorState(String deviceName, String osVersion, String osType)
            throws Throwable {
        Device device = getDevice(deviceName, osVersion, osType);
        return device.getState();
    }

    @Override
    public void bootSimulator(String deviceName, String osVersion, String osType)
            throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        commandPromptUtil.runCommandThruProcess("xcrun simctl boot " + simulatorUDID);
        commandPromptUtil.runCommandThruProcess("open -a Simulator --args -CurrentDeviceUDID "
                + simulatorUDID);
    }

    @Override
    public void installAppOnSimulator(String deviceName, String osVersion,
                                      String osType, String appPath) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        String execute = "xcrun simctl install " + simulatorUDID
                + " " + appPath;
        commandPromptUtil.execForProcessToExecute(execute);
    }

    @Override
    public void uninstallAppFromSimulator(String deviceName, String osVersion,
                                          String osType, String bundleID) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        String execute = "xcrun simctl uninstall " + simulatorUDID
                + " " + bundleID;
        commandPromptUtil.execForProcessToExecute(execute);
    }

    @Override
    public void createSimulator(String simName, String deviceName, String osVersion, String osType)
            throws Throwable {
        List<DeviceType> deviceTypes = getAllDeviceTypes();
        DeviceType deviceType = deviceTypes.stream().filter(d -> deviceName.equals(d.getName())).findFirst().get();
        List<Runtime> deviceRuntimes = getAllRuntimes();
        Runtime runtime = deviceRuntimes.stream().filter(r ->
                osType.equals(r.getOs()) && osVersion.equals(r.getVersion())
        ).findFirst().get();

        commandPromptUtil.runCommandThruProcess("xcrun simctl create " + simName + " " + deviceType.getIdentifier() + " " +
                runtime.getIdentifier());
    }

    @Override
    public void deleteSimulator(String deviceName, String osVersion, String osType)
            throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        commandPromptUtil.runCommandThruProcess("xcrun simctl delete " + simulatorUDID);
    }

    /**
     * Gets all available Simulators
     */
    private List<Device> getAllAvailableSimulators() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String simulatorJsonString = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j devices");

        JSONObject simulatorsJson = new JSONObject(simulatorJsonString);
        JSONObject devicesByTypeJson = (JSONObject) simulatorsJson.get("devices");
        Iterator<String> keys = devicesByTypeJson.keys();
        List<Device> deviceList = new ArrayList<>();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONArray devicesJson = (JSONArray) devicesByTypeJson.get(key);
            devicesJson.forEach(deviceJson -> {
                deviceList.add(new Device((JSONObject) deviceJson, key));
            });
        }
        return deviceList;
    }

    /**
     * Gets all available DeviceType
     */
    private List<DeviceType> getAllDeviceTypes() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String deviceTypesJSONString = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j devicetypes");

        JSONObject devicesTypesJSON = new JSONObject(deviceTypesJSONString);
        JSONArray devicesTypes = devicesTypesJSON.getJSONArray("devicetypes");
        List<DeviceType> deviceTypes = new ArrayList<>();
        devicesTypes.forEach(deviceType -> {
            String name = ((JSONObject) deviceType).getString("name");
            String identifier = ((JSONObject) deviceType).getString("identifier");
            deviceTypes.add(new DeviceType(name, identifier));
        });
        return deviceTypes;
    }

    /**
     * Gets all available Runtimes
     */
    private List<Runtime> getAllRuntimes() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String runtimesJSONString = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j runtimes");

        JSONArray runtimesJSON = new JSONObject(runtimesJSONString).getJSONArray("runtimes");
        List<Runtime> runtimes = new ArrayList<>();
        runtimesJSON.forEach(runtime -> {
            runtimes.add(new Runtime((JSONObject) runtime));
        });
        return runtimes;
    }

    private static <T> Collector<T, ?, List<T>> toList() {
        return Collectors.toCollection(ArrayList::new);
    }
}
