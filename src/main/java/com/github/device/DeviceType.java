package com.github.device;

public class DeviceType {
    private String name;
    private String identifier;

    public DeviceType(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
