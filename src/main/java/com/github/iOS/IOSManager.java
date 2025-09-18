package com.github.iOS;

import com.github.device.Device;
import com.github.interfaces.Manager;
import com.github.utils.CommandPromptUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IOSManager implements Manager {

    private CommandPromptUtil cmd;
    private final static int IOS_UDID_LENGTH = 40;
    private final static int SIM_UDID_LENGTH = 36;
    JSONObject iOSDevices;
    /*String profile = "system_profiler SPUSBDataType | sed -n -E -e '/(iPhone|iPad|iPod)/"
        + ",/Serial/s/ *Serial Number: *(.+)/\\1/p'";*/
    String profile = "ioreg -p IOUSB -l -w 0 | awk -F\\\" '/iPhone|iPad|iPod/ {found=1} found && /USB Serial Number/ {print $4; found=0}'";

    /*String profile = "idevice_id --list";*/

    public IOSManager() {
        cmd = new CommandPromptUtil();
        iOSDevices = new JSONObject();
    }

    @Override
    public Device getDevice(String udid) {
        Optional<Device> device = getDevices().stream().filter(d ->
                udid.equals(d.getUdid())).findFirst();
        return device.orElseThrow(() ->
                new RuntimeException("Provided DeviceUDID " + udid
                        + " is not found on the machine")
        );
    }

    public List<Device> getDevices() {
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

    public JSONObject getDeviceInfo(String udid) throws InterruptedException, IOException {

        String model = cmd.runProcessCommandToGetDeviceID("ideviceinfo -u "
                + udid + " | grep ProductType").replace("\n", "");

        String name = cmd.runProcessCommandToGetDeviceID("idevicename --udid " + udid);
        String osVersion = cmd.runProcessCommandToGetDeviceID("ideviceinfo --udid "
                        + udid
                        + " | grep ProductVersion").replace("ProductVersion:", "")
                .replace("\n", "").trim();

        iOSDevices.put("deviceModel", model);
        iOSDevices.put("udid", udid);
        iOSDevices.put("name", name);
        iOSDevices.put("brand", "Apple");
        iOSDevices.put("isDevice", "true");
        iOSDevices.put("screenSize", "Not Supported");
        iOSDevices.put("apiLevel", "");
        iOSDevices.put("osVersion", osVersion);
        iOSDevices.put("os", "iOS");
        iOSDevices.put("deviceManufacturer", "apple");
        return iOSDevices;
    }


    private List<String> getIOSUDID() {
        ArrayList<String> iosDeviceUDIDS = new ArrayList<>();
        try {
            Optional<String> getIOSDeviceID = Optional.of(cmd.runProcessCommandToGetDeviceID(profile));
            String[] iosDevices = getIOSDeviceID.get().split("\n");
            for (String iosDevice : iosDevices) {
                if (iosDevice.length() == 24) {
                    iosDeviceUDIDS.add(addChar(iosDevice, '-', 8));
                } else {
                    iosDeviceUDIDS.add(iosDevice);
                }
            }
            return iosDeviceUDIDS;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch iOS device connected");
        }

    }

    private String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }
}