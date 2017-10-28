package com.thoughtworks.interfaces;


import com.thoughtworks.device.Device;

import java.util.Optional;

public interface Manager {

    Device getDeviceProperties(String udid) throws Exception;
}
