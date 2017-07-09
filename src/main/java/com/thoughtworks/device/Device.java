package com.thoughtworks.device;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.utils.CommandPromptUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface Device {

    /**
     * Gets all available Simulators
     *
     */
    default Object getAllAvailableSimulators() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String fetchSimulators = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j devices");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(fetchSimulators, new TypeReference<Map<String, Object>>() {});
        return map.get("devices");
    }

    /**
     * Gets all available DeviceTypes
     *
     */
    default ArrayList<HashMap<Object, Object>> getAllDeviceTypes() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String fetchDeviceTypes = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j devicetypes");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(fetchDeviceTypes, new TypeReference<Map<String, Object>>() {});
        return (ArrayList<HashMap<Object, Object>>) map.get("devicetypes");
    }

    /**
     * Gets all available Runtimes
     *
     */
    default ArrayList<HashMap<Object, Object>> getAllRuntimes() throws IOException, InterruptedException {
        CommandPromptUtil commandPromptUtil = new CommandPromptUtil();
        String fetchDeviceTypes = commandPromptUtil.runCommandThruProcess("xcrun simctl list -j runtimes");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(fetchDeviceTypes, new TypeReference<Map<String, Object>>() {});
        return (ArrayList<HashMap<Object, Object>>) map.get("runtimes");
    }
}
