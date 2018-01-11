package com.thoughtworks.device;


import com.thoughtworks.android.AndroidManager;
import com.thoughtworks.iOS.IOSManager;
import com.thoughtworks.interfaces.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DeviceManager implements Manager {

    @Override
    public Device getDevice(String udid) throws Exception {
        Optional<Device> device = new AndroidManager().getDevices().stream().filter(d ->
                udid.equals(d.getUdid())).findFirst();
        Optional<Device> simulator = new SimulatorManager().getAllAvailableSimulators().stream().filter(sim ->
                udid.equals(sim.getUdid())).findFirst();
        Optional<Device> realDevice = new IOSManager().getDevices().stream().filter(sim ->
                udid.equals(sim.getUdid())).findFirst();
        Optional<Device> finalDeviceList = Optional.of(device
                .orElseGet(() -> simulator
                        .orElseGet(() -> realDevice
                                .orElseThrow(() -> new RuntimeException("No Results found")))));
        return finalDeviceList.get();
    }

    public List<Device> getDevices() throws Exception {
        List<Device> allDevice = new ArrayList<>();
        List<Device> androidDevice = new AndroidManager().getDevices();
        List<Device> iOSSimulators = new SimulatorManager().getAllBootedSimulators("iOS");
        List<Device> iOSRealDevice = new IOSManager().getDevices();
        Stream.of(androidDevice, iOSSimulators, iOSRealDevice).forEach(allDevice::addAll);
        return allDevice;
    }
}
