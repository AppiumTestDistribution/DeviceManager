package com.thoughtworks.interfaces;


import com.thoughtworks.device.Device;


public interface Manager {

    Device getDeviceProperties(String udid) throws Exception;
}
