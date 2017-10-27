package com.thoughtworks.device;


import com.thoughtworks.android.AndroidManager;

import java.util.List;
import java.util.Optional;

public class Manager implements com.thoughtworks.interfaces.Manager {

    @Override
    public Device getDeviceProperties(String udid) throws Exception {
        Optional<Device> device = new AndroidManager().getDeviceProperties().stream().filter(d ->
                udid.equals(d.getUdid())).findFirst();
        return device.orElse(new SimulatorManager().getSimulatorDetailsFromUDID(udid));
    }

    public List<Device> getDeviceProperties() throws Exception {
        List<Device> allDevice = new AndroidManager().getDeviceProperties();
        allDevice.addAll(new SimulatorManager().getAllBootedSimulators("iOS"));
        return allDevice;
    }
}
