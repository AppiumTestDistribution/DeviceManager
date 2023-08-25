package com.github.device;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class SimulatorTest {

    SimulatorManager simulatorManager;

    @Test
    public void getAllSimulatorsTest() throws IOException, InterruptedException {
        simulatorManager = new SimulatorManager();
        List<Device> allSimulators = simulatorManager.getAllSimulators("iOS");
        assertTrue(allSimulators.size() > 0);
    }


    @Test
    public void getSimulatorUDID() throws Throwable {
        simulatorManager = new SimulatorManager();
        String simulatorUDID = simulatorManager.getSimulatorUDID
                ("iPhone 14s", "11.4", "iOS");
        assertTrue(simulatorUDID.length() == 36);
    }

    @Test
    public void throwExceptionWhenInvalidOSVersionIsGiven() throws Throwable {
        simulatorManager = new SimulatorManager();
        try {
            simulatorManager.getSimulatorUDID
                    ("iPhone 14s", "9.99" , "iOS");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Device Not found with deviceName-iPhone 14s osVersion-9.99 osType-iOS");
        }
    }

    @Test
    public void throwExceptionWhenInvalidDeviceNameVersionIsGiven() throws Throwable {
        simulatorManager = new SimulatorManager();
        try {
            simulatorManager.getSimulatorUDID
                    ("iPhone 14ss", "16.4", "iOS");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Device Not found with deviceName-iPhone 14ss osVersion-16.4 osType-iOS");
        }
    }

    @Test
    public void getSimulatorStateTest() throws Throwable {
        simulatorManager = new SimulatorManager();
        String simulatorState = simulatorManager.getSimulatorState(
                "iPhone 14s", "16.4", "iOS");
        assertNotNull(simulatorState);
    }

    @Test
    public void bootSimulatorAndCheckStatus() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.bootSimulator(
                "iPhone 14", "16.4", "iOS");
        String deviceState = simulatorManager.getSimulatorState("iPhone 14",
                "16.4", "iOS");
        assertEquals(deviceState,"Booted");
    }

    @Test
    public void installApp() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.installAppOnSimulator("iPhone 14", "16.4", "iOS"
        ,System.getProperty("user.dir") + "/VodQAReactNative.app");
    }

    @Test
    public void uninstallApp() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.uninstallAppFromSimulator("iPhone 14", "16.4", "iOS"
                , "com.hariharanweb");
    }

    @Test
    public void createAndDeleteSimulatorTest() throws Throwable {
        simulatorManager = new SimulatorManager();
        long randomNumber = System.currentTimeMillis();
        String deviceName = "srini" + randomNumber;

        simulatorManager.createSimulator(deviceName, "iPhone 14", "16.4","iOS");
        assertNotNull(simulatorManager.getSimulatorState(deviceName, "16.4", "iOS"));
        simulatorManager.deleteSimulator(deviceName, "16.4", "iOS");
        try {
            simulatorManager.deleteSimulator(deviceName, "16.4", "iOS");
        } catch (Exception e){
            assertEquals("Device Not found with deviceName-" + deviceName + " osVersion-16.4 osType-iOS", e.getMessage());
        }
    }

    @Test
    public void getDeviceDetails() throws Throwable {
        simulatorManager = new SimulatorManager();
        String iOSUDID = simulatorManager.getSimulatorUDID
                ("iPhone 14s", "11.3", "iOS");
        Device deviceDetails = simulatorManager.getSimulatorDetailsFromUDID(iOSUDID);
        assertEquals(deviceDetails.getName(),"iPhone 14s");
        assertEquals(deviceDetails.getDeviceModel(),"iPhone8,1");
    }

    @Test
    public void captureScreenshot() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.bootSimulator(
                "iPhone 14", "16.4", "iOS");
        String iOSUDID = simulatorManager.getSimulatorUDID
                ("iPhone 14", "16.4", "iOS");
        simulatorManager.captureScreenshot(iOSUDID,"simulator",
                System.getProperty("user.dir") + "/target/", "jpeg");
        assertTrue(new File(System.getProperty("user.dir")
                + "/target/simulator.jpeg").exists());
    }

    @Test
    public void simulatorShutDown() throws IOException, InterruptedException {
        simulatorManager = new SimulatorManager();
        assertTrue(simulatorManager.shutDownAllBootedSimulators());
    }

    @Test
    public void getAllBootedSimulatorsTest() throws IOException, InterruptedException {
        simulatorManager = new SimulatorManager();
        assertEquals(simulatorManager.getAllBootedSimulators("iOS").get(0).getState(), "Booted");
    }

    @Test
    public void uploadMediaToSimulatorTest() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.uploadMediaToSimulator("iPhone 14", "16.4", "iOS",
                "/Users/ssekar/Desktop/GC_BCPage.png");
    }

    @Test
    public void getPropertiesTest() throws Exception {
        Device deviceProperties = new DeviceManager().getDevice("E2C34EB6-E91E-4148-8E95-73D53143E9EF");
        System.out.println(deviceProperties.getDeviceManufacturer());
    }

    @Test
    public void getDevicePropertiesTest() throws Exception {
//        List<Device> deviceProperties = new DeviceManager().getDevices();
//        System.out.println(deviceProperties.get(0).getName());
        new DeviceManager().getDevices();
    }

    @Test
    public void screenRecording() throws Exception {
        SimulatorManager simulatorManager = new SimulatorManager();
        simulatorManager.startScreenRecording("D97F6677-C9CE-45C5-87A1-B7C4B77C1D25","sample.mp4");
        Thread.sleep(10000);
        simulatorManager.stopScreenRecording();
        Assert.assertTrue(new File(System.getProperty("user.dir") + "/sample.mp4").exists());
    }

    @Test
    public void getOSandVersionLatest() throws Throwable {
        simulatorManager = new SimulatorManager();
        String iOSUDID = simulatorManager.getSimulatorUDID
                ("iPhone 14", "16.4", "iOS");
        Device deviceDetails = simulatorManager.getSimulatorDetailsFromUDID(iOSUDID);
        assertEquals(deviceDetails.getOsVersion(),"16.4");
        assertEquals(deviceDetails.getOs(),"iOS");
    }

}
