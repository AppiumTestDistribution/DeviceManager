package com.thoughtworks.iOS;

import com.thoughtworks.device.Device;
import com.thoughtworks.interfaces.Manager;
import com.thoughtworks.utils.CommandPromptUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IOSManager implements Manager {

    private CommandPromptUtil cmd;
    public static ArrayList<String> deviceUDIDiOS = new ArrayList<String>();
    private final static int IOS_UDID_LENGTH = 40;
    private final static int SIM_UDID_LENGTH = 36;
    JSONObject iOSDevices;
    String profile = "system_profiler SPUSBDataType | sed -n -E -e '/(iPhone|iPad|iPod)/"
            + ",/Serial/s/ *Serial Number: *(.+)/\\1/p'";

    public IOSManager() {
        cmd = new CommandPromptUtil();
        iOSDevices = new JSONObject();
    }

    @Override
    public Device getDeviceProperties(String udid) {
        Optional<Device> device = getAllAvailableDevices().stream().filter(d ->
                    udid.equals(d.getUdid())).findFirst();
        return device.orElseThrow(() ->
                new RuntimeException("Provided DeviceUDID " + udid
                        + " is not found on the machine")
        );
    }

    public List<Device> getAllAvailableDevices() {
        List<Device> device = new ArrayList<>();
        getIOSUDID().forEach(udid -> {
            try {
                device.add(new Device(getDeviceInfo(udid)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return device;
    }

    private JSONObject getDeviceInfo(String udid) throws InterruptedException, IOException {

        String model = cmd.runProcessCommandToGetDeviceID("ideviceinfo -u "
                + udid + " | grep ProductVersion").replace("\n", "");

        String name = cmd.runProcessCommandToGetDeviceID("idevicename --udid " + udid);
        String osVersion = cmd.runProcessCommandToGetDeviceID("ideviceinfo --udid "
                + udid
                + " | grep ProductVersion").replace("\n", "");

        iOSDevices.put("deviceModel",model);
        iOSDevices.put("udid",udid);
        iOSDevices.put("name",name);
        iOSDevices.put("brand","Apple");
        iOSDevices.put("isDevice","true");
        iOSDevices.put("screenSize","Not Supported");
        iOSDevices.put("apiLevel","");
        iOSDevices.put("osVersion",osVersion);
        return iOSDevices;
    }


    private ArrayList<String> getIOSUDID() {
        try {
            int startPos = 0;
            int endPos = IOS_UDID_LENGTH - 1;
            String getIOSDeviceID = cmd.runProcessCommandToGetDeviceID(profile);
            if (getIOSDeviceID == null || getIOSDeviceID.equalsIgnoreCase("") || getIOSDeviceID
                    .isEmpty()) {
            } else {
                while (endPos < getIOSDeviceID.length()) {
                    deviceUDIDiOS.add(getIOSDeviceID.substring(startPos, endPos + 1)
                            .replace("\n", ""));
                    startPos += IOS_UDID_LENGTH;
                    endPos += IOS_UDID_LENGTH;
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch iOS device connected");
        }
        return deviceUDIDiOS;
    }
}
