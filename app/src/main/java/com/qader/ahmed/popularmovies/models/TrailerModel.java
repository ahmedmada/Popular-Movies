package com.qader.ahmed.popularmovies.models;

import java.io.Serializable;



public class TrailerModel implements Serializable {
    private String name,key;

    public TrailerModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
