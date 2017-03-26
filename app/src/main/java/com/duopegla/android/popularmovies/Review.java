package com.duopegla.android.popularmovies;

/**
 * Created by duopegla on 26.3.2017..
 */

public class Review
{
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(String id, String author, String content, String url)
    {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getContent()
    {
        return this.content;
    }

    public String getAuthor()
    {
        return this.author;
    }
}
