package com.clipy;

import com.clipy.controller.CLIHandler;
import com.clipy.service.ClipboardMonitor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CLIHandler handler = new CLIHandler();
        ClipboardMonitor monitor = new ClipboardMonitor();
        Thread monitrorThread = new Thread(monitor);
        monitrorThread.setDaemon(true);
        monitrorThread.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            handler.handleCmd(input);

            if ("exit".equalsIgnoreCase(input)) {
                scanner.close();
                break;
            }
        }

    }
}