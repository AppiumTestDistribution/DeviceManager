package com.thoughtworks.device;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class SimulatorTest {

    DeviceManager deviceManager;

    @Test
    public void getAllSimulators() throws IOException, InterruptedException {
        deviceManager = new DeviceManager();
        HashMap<Object, Object> allSimulators = deviceManager.getAllSimulators();
        assertTrue(allSimulators.size() > 0);
    }
}
