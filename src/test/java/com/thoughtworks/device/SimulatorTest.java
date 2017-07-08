package com.thoughtworks.device;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimulatorTest {

    DeviceManager deviceManager;

    @Test
    public void getAllSimulatorsTest() throws IOException, InterruptedException {
        deviceManager = new DeviceManager();
        HashMap<Object, Object> allSimulators = deviceManager.getAllSimulators("iOS");
        assertTrue(allSimulators.size() > 0);
    }

    @Test
    public void getSimulatorUDID() throws Throwable {
        deviceManager = new DeviceManager();
        String simulatorUDID = deviceManager.getSimulatorUDID
                ("iPhone 6s", "11.0", "iOS");
        assertTrue(simulatorUDID.length() == 36);
    }

    @Test
    public void throwExceptionWhenInvalidOSVersionIsGiven() throws Throwable {
        deviceManager = new DeviceManager();
        try {
            deviceManager.getSimulatorUDID
                    ("iPhone 6s", "9.99" , "iOS");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Incorrect OS version is provided -- 9.99");
        }
    }

    @Test
    public void throwExceptionWhenInvalidDeviceNameVersionIsGiven() throws Throwable {
        deviceManager = new DeviceManager();
        try {
            deviceManager.getSimulatorUDID
                    ("iPhone 6ss", "11.0", "iOS");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Incorrect DeviceName is provided -- iPhone 6ss");
        }
    }

    @Test
    public void getSimulatorStateTest() throws Throwable {
        deviceManager = new DeviceManager();
        String simulatorState = deviceManager.getSimulatorState(
                "iPhone 6s", "10.1", "iOS");
        assertNotNull(simulatorState);
    }

    @Test
    public void bootSimulatorAndCheckStatus() throws Throwable {
        deviceManager = new DeviceManager();
        deviceManager.bootSimulator(
                "iPhone 6", "8.4", "iOS");
        String deviceState = deviceManager.getSimulatorState("iPhone 6",
                "8.4", "iOS");
        assertEquals(deviceState,"Booted");
    }
}
