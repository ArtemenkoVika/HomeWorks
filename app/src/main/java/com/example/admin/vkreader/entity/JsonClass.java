package com.example.admin.vkreader.entity;

import java.util.HashMap;

public class JsonClass {
    private String textContent;
    private String imageContent;
    private String textDate;
    private HashMap<String, String> map = new HashMap<String, String>();

    public JsonClass(String textContent, String textDate, String imageContent) {
        this.textContent = textContent;
        this.textDate = textDate;
        this.imageContent = imageContent;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getImageContent() {
        return imageContent;
    }

    public String getTextDate() {
        return textDate;
    }
}
