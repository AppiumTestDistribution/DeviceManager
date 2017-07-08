package com.thoughtworks.device;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulatorTest {

    DeviceManager deviceManager;

    @Test
    public void getAllSimulators() throws IOException, InterruptedException {
        deviceManager = new DeviceManager();
        HashMap<Object, Object> allSimulators = deviceManager.getAllSimulators();
        assertTrue(allSimulators.size() > 0);
    }

    @Test
    public void getSimulatorUDID() throws Throwable {
        deviceManager = new DeviceManager();
        String simulatorUDID = deviceManager.getSimulatorUDID
                ("iPhone 6s", "11.0");
        assertTrue(simulatorUDID.length() == 36);
    }

    @Test
    public void throwExceptionWhenInvalidOSVersionIsGiven() throws Throwable {
        deviceManager = new DeviceManager();
        try {
            deviceManager.getSimulatorUDID
                    ("iPhone 6s", "9.99");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Incorrect OS version is provided -- 9.99");
        }
    }

    @Test
    public void throwExceptionWhenInvalidDeviceNameVersionIsGiven() throws Throwable {
        deviceManager = new DeviceManager();
        try {
            deviceManager.getSimulatorUDID
                    ("iPhone 6ss", "11.0");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),"Incorrect DeviceName is provided -- iPhone 6ss");
        }
    }
}
