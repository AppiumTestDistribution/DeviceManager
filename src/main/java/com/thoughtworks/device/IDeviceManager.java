package com.thoughtworks.device;

import java.io.IOException;
import java.util.List;

public interface IDeviceManager {

    String getSimulatorState(String deviceName, String osVersion, String osType) throws Throwable;

    Device getDevice(String deviceName, String osVersion, String osType) throws InterruptedException, IOException;

    String getSimulatorUDID(String deviceName, String osVersion, String osType) throws Throwable;

    List<Device> getAllSimulators(String osType) throws InterruptedException, IOException;

    void deleteSimulator(String deviceName, String osVersion, String osType) throws Throwable;

    void createSimulator(String simName, String deviceName, String osVersion, String osType) throws Throwable;

    void uninstallAppFromSimulator(String deviceName, String osVersion, String osType, String bundleID) throws Throwable;

    void installAppOnSimulator(String deviceName, String osVersion, String osType, String appPath) throws Throwable;

    void bootSimulator(String deviceName, String osVersion, String osType) throws Throwable;
}
