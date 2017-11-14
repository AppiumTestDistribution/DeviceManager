package com.thoughtworks.interfaces;

import com.thoughtworks.device.Device;

import java.io.IOException;
import java.util.List;

public interface ISimulatorManager {

    String getSimulatorState(String deviceName, String osVersion, String osType) throws Throwable;

    Device getDevice(String deviceName, String osVersion, String osType) throws InterruptedException, IOException;

    String getSimulatorUDID(String deviceName, String osVersion, String osType) throws Throwable;

    List<Device> getAllSimulators(String osType) throws InterruptedException, IOException;

    void deleteSimulator(String deviceName, String osVersion, String osType) throws Throwable;

    void createSimulator(String simName, String deviceName, String osVersion, String osType) throws Throwable;

    void uninstallAppFromSimulator(String deviceName, String osVersion, String osType, String bundleID) throws Throwable;

    void installAppOnSimulator(String deviceName, String osVersion, String osType, String appPath) throws Throwable;

    void bootSimulator(String deviceName, String osVersion, String osType) throws Throwable;

    Device getSimulatorDetailsFromUDID(String UDID) throws IOException, InterruptedException;

    void captureScreenshot(String UDID, String fileName,String fileDestination, String format) throws IOException, InterruptedException;

    boolean shutDownAllBootedSimulators() throws IOException, InterruptedException;

    List<Device> getAllBootedSimulators(String osType) throws InterruptedException, IOException;

    void uploadMediaToSimulator(String deviceName, String osVersion, String osType, String filePath) throws Throwable;

    void startScreenRecording(String pathWithFileName) throws IOException;

    void stopScreenRecording() throws IOException, InterruptedException;

    void startScreenRecording(String UDID,String pathWithFileName) throws IOException;
}
