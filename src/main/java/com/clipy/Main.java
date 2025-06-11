package com.clipy;

import com.clipy.service.ClipboardMonitor;

public class Main {
    public static void main(String[] args) {
        // Start clipboard monitor in a daemon thread
        ClipboardMonitor monitor = new ClipboardMonitor();
        Thread monitorThread = new Thread(monitor);
        monitorThread.setDaemon(true);
        monitorThread.start();

    }
}