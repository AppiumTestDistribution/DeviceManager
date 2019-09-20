package com.github.device;

import lombok.Data;
import org.json.JSONObject;

@Data
public class IOSRuntime {
    private final String buildVersion;
    private final boolean isAvailable;
    private final String name;
    private final String identifier;
    private final String version;
    private final String os;

    public String getBuildVersion() {
        return buildVersion;
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


    public IOSRuntime(JSONObject runtimeJSON) {
        buildVersion = runtimeJSON.getString("buildversion");
        name = runtimeJSON.getString("name");
        version = runtimeJSON.getString("version");
        identifier = runtimeJSON.getString("identifier");
        isAvailable = runtimeJSON.getString("availability").equals("(available)");
        os = name.split(" ")[0];
    }
}
