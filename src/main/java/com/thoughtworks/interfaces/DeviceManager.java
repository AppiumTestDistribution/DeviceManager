package com.thoughtworks.interfaces;


import com.thoughtworks.device.Device;

public interface DeviceManager {

    Device getDeviceProperties(String udid) throws Exception;
}
