package com.clipy.model;

import java.time.LocalDateTime;

public class ClipboardItem {

    private final String content;
    private final String type;
    private final LocalDateTime timestamp;

    public ClipboardItem(String Message, String type){
        this.content = Message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public ClipboardItem(String Message, String type, LocalDateTime timestamp){
        this.content = Message;
        this.type = type;
        this.timestamp = timestamp;
    }



    @Override
    public String toString() {
        return "content: "+content+"\ntype: "+type+"\ntimestamp: "+timestamp.toString();
    }

    public String getContent(){return content; }
    public String getType(){return type; }
    public LocalDateTime getTimestamp(){return timestamp; }


}
