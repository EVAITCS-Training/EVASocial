package com.horrorcore.models;

import java.time.LocalDateTime;
import java.util.Arrays;

//Data Class, other names: Entities, Models
public class Post extends Object {
    // Field Variables
    // These variables are going to hold state data for the instance object
    private long id;
    private String status;
    private String username;
    private boolean isDraft;
    private int likes;
    private int dislikes;
    // Date(Old System) or LocalDate(New System/Java 8)
    //LocalDate, LocalDateTime, LocalTime
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Comment[] comments;
    private static long idCounter = 1;
    //Default Constructor
    //Constructors are special methods to initialize instance objects during runtime
    public Post() {}
    //Parameterized Constructor
    public Post(String status, String username) {
        this.id = idCounter++;
        this.status = status;
        this.username = username;
        this.isDraft = true;
        this.likes = 0;
        this.dislikes = 0;
        this.createdAt = LocalDateTime.now();
        this.comments = new Comment[10];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static long getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(long idCounter) {
        Post.idCounter = idCounter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", comments=" + Arrays.toString(comments) +
                '}';
    }
}
