package com.github.device;

import lombok.Data;
import org.json.JSONObject;

@Data
public class IOSRuntime {
    private final String buildversion;
    private final boolean isAvailable;
    private final String name;
    private final String identifier;
    private final String version;
    private final String os;

    public IOSRuntime(JSONObject runtimeJSON) {
        buildversion = runtimeJSON.getString("buildversion");
        name = runtimeJSON.getString("name");
        version = runtimeJSON.getString("version");
        identifier = runtimeJSON.getString("identifier");
        isAvailable = runtimeJSON.getString("availability").equals("(available)");
        os = name.split(" ")[0];
    }
}
