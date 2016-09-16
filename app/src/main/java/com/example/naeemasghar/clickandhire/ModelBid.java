package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 5/26/2016.
 */
public class ModelBid {
    public String bId="";
    public String uId="";
    public String tId="";
    public String name = "";
    public String user_bid = "";
    public String date = "";

    public String getbId() {
        return bId;
    }

    public void setbId(String id) {
        this.bId = id;
    }
    public String getuId() {
        return uId;
    }

    public void setuId(String id) {
        this.uId = id;
    }
    public String gettId() {
        return tId;
    }

    public void settId(String id) {
        this.tId = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUser_bid() {
        return user_bid;
    }

    public void setUser_bid(String bid) {
        this.user_bid = bid;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
