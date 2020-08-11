package com.example.crossprocesseventbus.client;

public class Message implements IMessage {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
