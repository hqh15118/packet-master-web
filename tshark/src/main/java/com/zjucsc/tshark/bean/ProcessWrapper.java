package com.zjucsc.tshark.bean;

public class ProcessWrapper {
    private Process process;
    private String command;

    public ProcessWrapper(Process process, String command) {
        this.process = process;
        this.command = command;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
