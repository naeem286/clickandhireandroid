package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/14/2016.
 */
public class SubCategory {
    private String title;
    private String numOfProjects;
    private String thumbnail;

    public SubCategory() {
    }

    public SubCategory(String title, String numOfProjects, String thumbnail) {
        this.title = title;
        this.numOfProjects = numOfProjects;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumOfProjects() {
        return numOfProjects;
    }

    public void setNumOfProjects(String numOfSongs) {
        this.numOfProjects = numOfSongs;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

