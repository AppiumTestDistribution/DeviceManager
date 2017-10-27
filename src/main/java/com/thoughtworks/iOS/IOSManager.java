package com.thoughtworks.iOS;

import com.thoughtworks.device.Device;
import com.thoughtworks.interfaces.Manager;
import com.thoughtworks.utils.CommandPromptUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOSManager implements Manager {

    private CommandPromptUtil cmd;
    public static ArrayList<String> deviceUDIDiOS = new ArrayList<String>();
    private final static int IOS_UDID_LENGTH = 40;
    private final static int SIM_UDID_LENGTH = 36;
    String profile = "system_profiler SPUSBDataType | sed -n -E -e '/(iPhone|iPad|iPod)/"
            + ",/Serial/s/ *Serial Number: *(.+)/\\1/p'";

    public IOSManager() {
        cmd = new CommandPromptUtil();
    }

    @Override
    public Device getDeviceProperties(String udid) throws Exception {


        return null;
    }

    public List<Device> getAllAvailableDevices() throws IOException, InterruptedException {
        getIOSUDID().forEach(deviceUDID -> {
            System.out.println(deviceUDID);
        } );
        return null;
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
