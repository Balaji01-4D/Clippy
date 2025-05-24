package com.clipy.service;

import com.clipy.model.ClipboardItem;
import com.clipy.repository.ClipboardDB;

import java.util.List;

public class ClippyService {

    private final ClipboardDB repository;
    private static ClippyService service;


    private ClippyService() {
        this.repository = new ClipboardDB();
    }

    public static ClippyService getInstance(){
        if (service == null) service = new ClippyService();
        return service;
    }

    public List<ClipboardItem> getAllItems(){
        return repository.getAllItems();
    }

    public void addItem(ClipboardItem item){
        repository.addItem(item);
    }

    public void truncateTable(){
        repository.truncateTable();
    }

    public void closeConnection() {
        repository.closeConnection();
    }

    public List<ClipboardItem> getByType(String type){
        return repository.getByType(type);
    }
}
