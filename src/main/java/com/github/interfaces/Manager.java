package com.github.interfaces;


import com.github.device.Device;

import java.util.List;


public interface Manager {

    Device getDevice(String udid) throws Exception;

    List<Device> getDevices() throws Exception;
}
