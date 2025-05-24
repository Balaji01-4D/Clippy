package com.clipy.controller;

import com.clipy.model.ClipboardItem;
import com.clipy.service.ClippyService;

import java.util.List;

public class CLIHandler {

    private static ClippyService service;

    public CLIHandler(){
        service = ClippyService.getInstance();
    }

    public void handleCmd(String command) {
        switch (command.trim().toLowerCase()) {
            case "list" -> printList();
            case "list code" -> printByType("code");
            case "list text" -> printByType("text");
            case "list url" -> printByType("url");
            case "exit" -> exit();
            case "clear" -> truncate();
            default -> System.out.println("Unknown command. Try 'list type(optional)' or 'exit'.");
        };
    }

    private void exit(){
        System.out.println("Exiting program...");
        service.closeConnection();
    }

    private void printList() {
        List<ClipboardItem> items = service.getAllItems();
        for (ClipboardItem clip : items) {
            System.out.println(clip);
        }
    }

    private void truncate(){
        service.truncateTable();
    }

    private void printByType(String type){
        List<ClipboardItem> items = service.getByType(type);
        for (ClipboardItem item : items){
            System.out.println(item);
        }
    }


}
