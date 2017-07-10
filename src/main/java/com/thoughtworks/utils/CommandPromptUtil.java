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

    private BufferedReader getBufferedReader(String command) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add(command);
        ProcessBuilder builder = new ProcessBuilder(commands);
        final Process process = builder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    public void execForProcessToExecute(String cmd) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while((line=input.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

}
