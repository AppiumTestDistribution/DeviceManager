package com.thoughtworks.interfaces;


import com.thoughtworks.device.Device;

import java.io.IOException;

public interface DeviceManager {

    Device getDeviceProperties(String udid) throws Exception;

    void installApp(String apkPath) throws Exception;

    void installApp(String apkPath,String udid) throws IOException, InterruptedException;
}
