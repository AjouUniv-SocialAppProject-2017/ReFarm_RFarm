package com.example.byoungeun_kim.exrefarm;

/**
 * Created by byoungeun-Kim on 2017. 11. 20..
 */

public class Post {

    public String email;

    public String title;

    public String content;

    public String photo;

    public String key;

    public String comment;


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Post(String comment) {
        this.comment = comment;
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
