package com.thoughtworks.device;


import com.thoughtworks.android.AndroidManager;
import com.thoughtworks.iOS.IOSManager;

import java.util.List;
import java.util.Optional;

public class Manager implements com.thoughtworks.interfaces.Manager {

    @Override
    public Device getDeviceProperties(String udid) throws Exception {
        Optional<Device> device = new AndroidManager().getDeviceProperties().stream().filter(d ->
                udid.equals(d.getUdid())).findFirst();
        Optional<Device> simulator = new SimulatorManager().getAllAvailableSimulators().stream().filter(sim ->
                udid.equals(sim.getUdid())).findFirst();
        Optional<Device> realDevice = new IOSManager().getAllAvailableDevices().stream().filter(sim ->
                udid.equals(sim.getUdid())).findFirst();
        Optional<Device> finalDeviceList = Optional.of(device
                .orElseGet(() -> simulator
                        .orElseGet(() -> realDevice
                                .orElseThrow(() -> new RuntimeException("No Results found")))));
        return finalDeviceList.get();
    }

    public List<Device> getDeviceProperties() throws Exception {
        List<Device> allDevice = new AndroidManager().getDeviceProperties();
        allDevice.addAll(new SimulatorManager().getAllBootedSimulators("iOS"));
        return allDevice;
    }
}
