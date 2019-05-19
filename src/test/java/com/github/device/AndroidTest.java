package com.github.device;


import com.github.android.AndroidManager;
import org.junit.Test;

public class AndroidTest {

    AndroidManager deviceManager;


    @Test
    public void getDetails() throws Exception {
        deviceManager = new AndroidManager();
        Device deviceProperties = deviceManager.getDevice("192.168.58.101:5555");
        System.out.println(deviceProperties.isDevice());
        System.out.println(deviceProperties.getScreenSize());
    }
}
