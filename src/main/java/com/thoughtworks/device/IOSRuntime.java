package com.thoughtworks.device;

import org.json.JSONObject;

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

    public String getBuildversion() {
        return buildversion;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getVersion() {
        return version;
    }

    public String getOs() {
        return os;
    }
}
