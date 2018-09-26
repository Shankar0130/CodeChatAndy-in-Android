package com.shankaryadav.www.codechatandy;

public class InstantMessage {
    private String author;
    private String message;

    public InstantMessage(String message, String author) {
        this.author = author;
        this.message = message;
    }


    public InstantMessage() {
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
