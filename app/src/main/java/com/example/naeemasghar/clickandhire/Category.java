package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/10/2016.
 */
public class Category {
    private String title;
    private String id;
    private int numOfProjects;
    private int thumbnail;

    public Category() {
    }

    public Category(String title, String id, int numOfProjects, int thumbnail) {
        this.title = title;
        this.id=id;
        this.numOfProjects = numOfProjects;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumOfProjects() {
        return numOfProjects;
    }

    public void setNumOfProjects(int numOfSongs) {
        this.numOfProjects = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
