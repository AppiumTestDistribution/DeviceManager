package com.thoughtworks.device;


import com.thoughtworks.android.AndroidManager;
import org.junit.Test;

public class AndroidTest {

    AndroidManager deviceManager;


    @Test
    public void getDetails() throws Exception {
        deviceManager = new AndroidManager();
        Device deviceProperties = deviceManager.getDeviceProperties("192.168.58.101:5555");
        System.out.println(deviceProperties.getIsDevice());
        System.out.println(deviceProperties.getScreenSize());
    }
}
