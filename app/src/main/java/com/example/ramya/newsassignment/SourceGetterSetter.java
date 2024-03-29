package com.example.ramya.newsassignment;

import java.io.Serializable;


class SourceGetterSetter implements Serializable
{
    String id;
    String name;
    String url;
    String cat;



    public SourceGetterSetter(String id, String name, String url, String category) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.cat = category;
    }


    public SourceGetterSetter(String category)
    {
        this.cat = category;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return cat;
    }

    public void setCategory(String category) {
        this.cat = category;
    }
}
