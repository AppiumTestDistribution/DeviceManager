package com.thoughtworks.interfaces;


import com.thoughtworks.device.Device;

import java.util.List;


public interface Manager {

    Device getDevice(String udid) throws Exception;

    List<Device> getDevices() throws Exception;
}
