package com.qader.ahmed.popularmovies;

import java.io.Serializable;


public class TrailerData implements Serializable{
    private String key;
    private String name;
    private String site;
    private String type;

    public String getKey() {
        return "https://www.youtube.com/watch?v="+key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
