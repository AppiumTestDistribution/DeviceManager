package com.github.device;

import com.github.iOS.IOSManager;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class IOSDeviceTest {

    @Test
    public void getDevices() {
        IOSManager ios = new IOSManager();
        List<Device> devices = ios.getDevices();
        assertTrue(devices.size() > 0);
    }
}
