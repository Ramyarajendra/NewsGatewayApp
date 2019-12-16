package com.example.ramya.newsassignment;

import java.io.Serializable;

class Article implements Serializable
{
    private String urlimg;
    private String pubat;
    private String auth;
    private String title;
    private String desc;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public Article(String author, String title, String description, String urlToImage, String publishedAt, String url)
    {
        this.auth = author;
        this.title = title;
        this.desc = description;
        this.urlimg = urlToImage;
        this.pubat = publishedAt;
        this.url = url;
    }

    public Article(String author, String title, String description, String urlToImage)
    {
        this.auth = author;
        this.title = title;
        this.desc = description;
        this.urlimg = urlToImage;
    }

    public String getAuthor() {
        return auth;
    }

    public void setAuthor(String author) {
        this.auth = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public String getUrlToImage() {
        return urlimg;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlimg = urlToImage;
    }

    public String getPublishedAt() {
        return pubat;
    }

    public void setPublishedAt(String publishedAt) {
        this.pubat = publishedAt;
    }
}
