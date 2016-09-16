package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/14/2016.
 */
public class Work {
    private String title, date, detail;

    public Work() {
    }

    public Work(String title, String detail, String date) {
        this.title = title;
        this.detail = detail;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
