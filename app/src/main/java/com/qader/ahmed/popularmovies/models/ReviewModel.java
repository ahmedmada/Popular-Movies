package com.qader.ahmed.popularmovies.models;

import java.io.Serializable;



public class ReviewModel implements Serializable {

    private String author,content;

    public ReviewModel() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
