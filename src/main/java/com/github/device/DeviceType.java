package com.github.device;

import lombok.Data;

@Data
public class DeviceType {
    private String name;
    private String identifier;

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public DeviceType(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
    }

}
