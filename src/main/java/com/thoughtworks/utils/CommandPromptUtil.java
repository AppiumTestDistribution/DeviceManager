package com.thoughtworks.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CommandPromptUtil {

    public String runCommandThruProcess(String command)
            throws InterruptedException, IOException {
        BufferedReader br = getBufferedReader(command);
        String line;
        String allLine = "";
        while ((line = br.readLine()) != null) {
            allLine = allLine + "" + line + "\n";
        }
        return allLine;
    }

    public List<String> runCommand(String command)
            throws InterruptedException, IOException {
        BufferedReader br = getBufferedReader(command);
        String line;
        List<String> allLine = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            allLine.add(line.replaceAll("[\\[\\](){}]","_").split("_")[1]);
        }
        return allLine;
    }

    private BufferedReader getBufferedReader(String command) throws IOException {
        List<String> commands = new ArrayList<>();
       //commands.add("/bin/sh");
        commands.add("cmd");      // Replace "cmd" by "/bin/sh" is you are running on MAC
        //commands.add("-c");
        commands.add("/c");       // Replace "/c" by "-c" is you are running on MAC
        commands.add(command);
        ProcessBuilder builder = new ProcessBuilder(commands);
        final Process process = builder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    public Process execForProcessToExecute(String cmd) throws IOException {
        Process pr = null;
        List<String> commands = new ArrayList<>();
        //commands.add("/bin/sh");
        commands.add("cmd");        // Replace "cmd" by "/bin/sh" is you are running on MAC
        commands.add("/c");         // Replace "/c" by "-c" is you are running on MAC
        //commands.add("-c");
        commands.add(cmd);
        ProcessBuilder builder = new ProcessBuilder(commands);
        pr = builder.start();
        return pr;
    }

    public String runProcessCommandToGetDeviceID(String command)
            throws InterruptedException, IOException {
        BufferedReader br = getBufferedReader(command);
        String line;
        String allLine = "";
        while ((line = br.readLine()) != null) {
            allLine = allLine.trim() + "" + line.trim() + "\n";
        }
        return allLine.trim();
    }

}
