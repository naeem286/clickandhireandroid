package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 7/14/2016.
 */
public class ModelRating {
    public String uId="";
    public String ruId="";
    public String name = "";
    public String stars = "";
    public String avtRating="";
    public String comment = "";
    public String date = "";


    public String getuId() {
        return uId;
    }

    public void setuId(String id) {
        this.uId = id;
    }
    public String getRuId() {
        return ruId;
    }

    public void setRuId(String id) {
        this.ruId = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
    public String getAvtRating() {
        return avtRating;
    }

    public void setAvtRating(String rating) {
        this.avtRating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
