package com.example.developmenttest.model.entities;

import com.google.gson.annotations.SerializedName;

public class Color {

    private String from;
    private String to;
    private String color;

    public Color(String from, String to, String color) {
        setFrom(from);
        setTo(to);
        setColor(color);
    }

    public String getFrom() {
        return from;
    }

    private void setFrom(String from) {
        if (!from.isEmpty())
            this.from = from;
    }

    public String getTo() {
        return to;
    }

    private void setTo(String to) {
        if (!to.isEmpty())
            this.to = to;
    }

    public String getColor() {
        return color;
    }

    private void setColor(String color) {
        if (!color.isEmpty())
            this.color = color;
    }
}