package com.clipy.service;

import com.clipy.model.ClipboardItem;
import java.awt.*;
import java.awt.datatransfer.*;

public class ClipboardMonitor implements Runnable {
    private final ClippyService service;
    private String lastContent = "";

    public ClipboardMonitor() {
        this.service = ClippyService.getInstance();
    }

    @Override
    public void run() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        while (true) {
            try {
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    String current = (String) clipboard.getData(DataFlavor.stringFlavor);

                    if (current != null && !current.equals(lastContent)) {
                        lastContent = current;

                        String type = detectType(current);

                        ClipboardItem item = new ClipboardItem(current, type);
                        service.addItem(item);
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }

    private static String detectType(String text) {
        if (text.startsWith("http://") || text.startsWith("https://") || text.startsWith("www.")) return "url";
        if (text.contains("{") || text.contains("}")) return "code";
        return "text";
    }


}
