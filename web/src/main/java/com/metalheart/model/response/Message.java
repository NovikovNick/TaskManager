package com.metalheart.model.response;

import lombok.Data;

@Data
public class Message {

    private Type type;

    private String payload;

    public Message(String payload) {
        this.type = Type.INFO;
        this.payload = payload;
    }

    public enum Type {
        INFO, ERROR
    }
}
