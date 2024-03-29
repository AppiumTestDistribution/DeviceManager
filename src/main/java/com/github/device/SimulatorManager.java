package com.github.device;

import com.github.interfaces.ISimulatorManager;
import com.github.utils.CommandPromptUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SimulatorManager implements ISimulatorManager {

    final static Logger logger = LogManager.getLogger(SimulatorManager.class);
    private CommandPromptUtil commandPromptUtil;
    String ANSI_RED_BACKGROUND = "\u001B[41m";
    Process screenRecordProcess;

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
        return device.orElseThrow(() ->
                new RuntimeException("Device Not found with deviceName-" + deviceName + " osVersion-" + osVersion + " osType-" + osType)
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
        logger.debug(ANSI_RED_BACKGROUND + "Waiting for Simulator to Boot Completely.....");
        commandPromptUtil.runCommandThruProcess("xcrun simctl launch booted com.apple.springboard");
        commandPromptUtil.runCommandThruProcess("open -a Simulator --args -CurrentDeviceUDID "
                + simulatorUDID);
    }

    @Override
    public Device getSimulatorDetailsFromUDID(String UDID){
        List<Device> allSimulators = null;
        try {
            allSimulators = getAllAvailableSimulators();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Optional<Device> device = allSimulators.stream().filter(d ->
                UDID.equals(d.getUdid())).findFirst();
        return device.orElseThrow(() ->
                new RuntimeException("Device Not found")
        );
    }

    @Override
    public void captureScreenshot(String UDID, String fileName, String fileDestination, String format) throws IOException, InterruptedException {
        String xcodeVersion = commandPromptUtil.runCommandThruProcess("xcodebuild -version").split("(\\n)|(Xcode)")[1].trim();
        if (Float.valueOf(xcodeVersion) < 8.2 ) {
            new RuntimeException("Screenshot capture is only supported with xcode version 8.2 and above");
        } else {
            commandPromptUtil.runCommandThruProcess("xcrun simctl io " + UDID + " screenshot "
                    + fileDestination + "/" + fileName + "." + format);
        }
    }

    @Override
    public boolean shutDownAllBootedSimulators() throws IOException, InterruptedException {
        List<String> bootedDevices = commandPromptUtil.runCommand("xcrun simctl list | grep Booted");
        bootedDevices
                .forEach(bootedUDID -> {
                    try {
                        commandPromptUtil.runCommandThruProcess("xcrun simctl shutdown " + bootedUDID);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        String bootedDeviceCountAfterShutDown = commandPromptUtil.
                runCommandThruProcess("xcrun simctl list | grep Booted | wc -l");
        if (Integer.valueOf(bootedDeviceCountAfterShutDown.trim()) == 0 ) {
            logger.debug(ANSI_RED_BACKGROUND + "All Booted Simulators Shut...");
            return true;
        } else {
            logger.debug(ANSI_RED_BACKGROUND + "Simulators that needs to be ShutDown are"
                    + commandPromptUtil.runCommand("xcrun simctl list | grep Booted"));
            return false;
        }
    }

    @Override
    public List<Device> getAllBootedSimulators(String osType) throws InterruptedException, IOException {
        List<Device> allSimulators = getAllSimulators(osType);
        List<Device> bootedSim = allSimulators.stream().filter( device ->
                device.getState().equalsIgnoreCase("Booted")).collect(Collectors.toList());
        return bootedSim;
    }

    @Override
    public void uploadMediaToSimulator(String deviceName, String osVersion, String osType, String filePath) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        String execute = "xcrun simctl addmedia " + simulatorUDID
                + " " + filePath;
        commandPromptUtil.execForProcessToExecute(execute).waitFor();
    }

    @Override
    public void startScreenRecording(String pathWithFileName) throws IOException {
        System.out.println("xcrun simctl io booted recordVideo " + pathWithFileName);
        screenRecordProcess = commandPromptUtil
                .execForProcessToExecute("xcrun simctl io booted recordVideo " + pathWithFileName);
        System.out.println(screenRecordProcess);
    }

    @Override
    public Process startScreenRecording(String UDID, String pathWithFileName) throws IOException {
        System.out.println("xcrun simctl io " + UDID + " recordVideo " + pathWithFileName);
        screenRecordProcess = commandPromptUtil
                .execForProcessToExecute("xcrun simctl io " + UDID + " recordVideo " + pathWithFileName);
        System.out.println(screenRecordProcess);
        return screenRecordProcess;
    }

    @Override
    public void stopScreenRecording() throws IOException, InterruptedException {
        Integer processId = getPid(screenRecordProcess);
        String command = "kill -9 " + processId;
        System.out.println("Stopping Video Recording" + command);
        commandPromptUtil.execForProcessToExecute(command);
    }

    @Override
    public void installAppOnSimulator(String deviceName, String osVersion,
                                      String osType, String appPath) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        String execute = "xcrun simctl install " + simulatorUDID
                + " " + appPath;
        commandPromptUtil.execForProcessToExecute(execute).waitFor();
    }

    @Override
    public void uninstallAppFromSimulator(String deviceName, String osVersion,
                                          String osType, String bundleID) throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        String execute = "xcrun simctl uninstall " + simulatorUDID
                + " " + bundleID;
        commandPromptUtil.execForProcessToExecute(execute).waitFor();
    }

    @Override
    public void createSimulator(String simName, String deviceName, String osVersion, String osType)
            throws Throwable {
        List<DeviceType> deviceTypes = getAllDeviceTypes();
        DeviceType deviceType = deviceTypes.stream().filter(d -> deviceName.equals(d.getName())).findFirst().get();
        List<IOSRuntime> deviceIOSRuntimes = getAllRuntimes();
        IOSRuntime IOSRuntime = deviceIOSRuntimes.stream().filter(r ->
                osType.equals(r.getOs()) && osVersion.equals(r.getVersion())
        ).findFirst().get();

        commandPromptUtil.runCommandThruProcess("xcrun simctl create " + simName + " " + deviceType.getIdentifier() + " " +
                IOSRuntime.getIdentifier());
    }

    @Override
    public void deleteSimulator(String deviceName, String osVersion, String osType)
            throws Throwable {
        String simulatorUDID = getSimulatorUDID(deviceName, osVersion, osType);
        commandPromptUtil.runCommandThruProcess("xcrun simctl delete " + simulatorUDID);
    }


    public List<Device> getAllAvailableSimulators() throws IOException, InterruptedException {
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


    private List<IOSRuntime> getAllRuntimes() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String runtimesJSONString = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j runtimes");

        JSONArray runtimesJSON = new JSONObject(runtimesJSONString).getJSONArray("runtimes");
        List<IOSRuntime> IOSRuntimes = new ArrayList<>();
        runtimesJSON.forEach(runtime -> {
            IOSRuntimes.add(new IOSRuntime((JSONObject) runtime));
        });
        return IOSRuntimes;
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

    private static <T> Collector<T, ?, List<T>> toList() {
        return Collectors.toCollection(ArrayList::new);
    }
}
