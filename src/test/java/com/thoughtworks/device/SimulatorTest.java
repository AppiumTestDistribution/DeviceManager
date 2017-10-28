package com.thoughtworks.device;

import com.thoughtworks.iOS.IOSManager;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
                ("iPhone 6s", "11.0", "iOS");
        assertTrue(simulatorUDID.length() == 36);
    }

    @Test
    public void throwExceptionWhenInvalidOSVersionIsGiven() throws Throwable {
        simulatorManager = new SimulatorManager();
        try {
            simulatorManager.getSimulatorUDID
                    ("iPhone 6s", "9.99" , "iOS");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Device Not found with deviceName-iPhone 6s osVersion-9.99 osType-iOS");
        }
    }

    @Test
    public void throwExceptionWhenInvalidDeviceNameVersionIsGiven() throws Throwable {
        simulatorManager = new SimulatorManager();
        try {
            simulatorManager.getSimulatorUDID
                    ("iPhone 6ss", "11.0", "iOS");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Device Not found with deviceName-iPhone 6ss osVersion-11.0 osType-iOS");
        }
    }

    @Test
    public void getSimulatorStateTest() throws Throwable {
        simulatorManager = new SimulatorManager();
        String simulatorState = simulatorManager.getSimulatorState(
                "iPhone 6s", "11.0", "iOS");
        assertNotNull(simulatorState);
    }

    @Test
    public void bootSimulatorAndCheckStatus() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.bootSimulator(
                "iPhone 6", "11.0", "iOS");
        String deviceState = simulatorManager.getSimulatorState("iPhone 6",
                "11.0", "iOS");
        assertEquals(deviceState,"Booted");
    }

    @Test
    public void installApp() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.installAppOnSimulator("iPhone 6", "11.0", "iOS"
        ,System.getProperty("user.dir") + "/VodQAReactNative.app");
    }

    @Test
    public void uninstallApp() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.uninstallAppFromSimulator("iPhone 6", "11.0", "iOS"
                , "com.hariharanweb");
    }

    @Test
    public void createAndDeleteSimulatorTest() throws Throwable {
        simulatorManager = new SimulatorManager();
        long randomNumber = System.currentTimeMillis();
        String deviceName = "srini" + randomNumber;

        simulatorManager.createSimulator(deviceName, "iPhone 6", "11.0","iOS");
        assertNotNull(simulatorManager.getSimulatorState(deviceName, "11.0", "iOS"));
        simulatorManager.deleteSimulator(deviceName, "11.0", "iOS");
        try {
            simulatorManager.deleteSimulator(deviceName, "11.0", "iOS");
        } catch (Exception e){
            assertEquals("Device Not found with deviceName-" + deviceName + " osVersion-11.0 osType-iOS", e.getMessage());
        }
    }

    @Test
    public void getDeviceDetails() throws Throwable {
        simulatorManager = new SimulatorManager();
        String iOSUDID = simulatorManager.getSimulatorUDID
                ("iPhone 6s", "10.1", "iOS");
        Device deviceDetails = simulatorManager.getSimulatorDetailsFromUDID(iOSUDID);
        assertEquals(deviceDetails.getName(),"iPhone 6s");
    }

    @Test
    public void captureScreenshot() throws Throwable {
        simulatorManager = new SimulatorManager();
        simulatorManager.bootSimulator(
                "iPhone 6", "11.0", "iOS");
        String iOSUDID = simulatorManager.getSimulatorUDID
                ("iPhone 6", "11.0", "iOS");
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
        simulatorManager.uploadMediaToSimulator("iPhone 6", "11.0", "iOS",
                "/Users/ssekar/Desktop/GC_BCPage.png");
    }

    @Test
    public void getPropertiesTest() throws Exception {
        Device deviceProperties = new Manager().getDeviceProperties("157BFA56-AF97-4DFF-8B98-794EF4ED9E81");
        System.out.println(deviceProperties.getName());
    }

    @Test
    public void getDevicePropertiesTest() throws Exception {
//        List<Device> deviceProperties = new Manager().getDeviceProperties();
//        System.out.println(deviceProperties.get(0).getName());
        new IOSManager().getAllAvailableDevices();
    }
}
