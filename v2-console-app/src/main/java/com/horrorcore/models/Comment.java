package com.horrorcore.models;

import java.time.LocalDateTime;

/**
 * Simple comment model attached to posts. Comments validate that the message is
 * non-blank and assign an auto-increment id from a static counter.
 */
public class Comment {
    private long id;
    private String message;
    private static long idCounter = 1;
    private LocalDateTime postDate;

    public Comment() {

    }

    public Comment(String message) {
        if (message.isBlank()) throw new IllegalArgumentException("Comments have to be filled out!");
        this.message = message;
        this.id = idCounter++;
        this.postDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return message;
    }

    public long getId() {
        return id;
    }

    public static long getIdCounter() {
        return idCounter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message.isBlank()) throw new IllegalArgumentException("Comments have to be filled out!");
        this.message = message;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }
}
