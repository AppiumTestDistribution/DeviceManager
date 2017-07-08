package com.thoughtworks.device;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.utils.CommandPromptUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeviceManager {

    CommandPromptUtil commandPromptUtil;
    private HashMap<Object, Object> availableSimulators;

    public DeviceManager() {
        commandPromptUtil = new CommandPromptUtil();
        availableSimulators = new HashMap<>();
    }

    public HashMap<Object, Object> getAllSimulators()
            throws InterruptedException, IOException {
        String fetchSimulators = commandPromptUtil.
                runCommandThruProcess("xcrun simctl list -j devices");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map;
        map = mapper.readValue(fetchSimulators, new TypeReference<Map<String, Object>>() {});
        Object devices = map.get("devices");
        ((LinkedHashMap) devices).forEach((key, value) -> {
            if(((String) key).split(" ")[0].equals("iOS")) {
                availableSimulators.put(key,value);
            }
        });
        return availableSimulators;
    }
}
