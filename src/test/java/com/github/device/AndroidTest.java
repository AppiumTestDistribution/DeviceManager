package com.github.device;


import com.github.android.AndroidManager;
import org.junit.Test;

import java.util.List;

public class AndroidTest {

    private AndroidManager deviceManager;


    @Test
    public void getDetails() throws Exception {
        deviceManager = new AndroidManager();
        List<Device> devices = deviceManager.getDevices();
        if (devices.size() >0){
            Device deviceProperties = deviceManager.getDevice(devices.get(0).getUdid());
            System.out.println(deviceProperties.isDevice());
            System.out.println(deviceProperties.getScreenSize());
            System.out.println(deviceProperties.getName());
        }else {
            throw new Exception("No device attached with the system");
        }

    }
}
