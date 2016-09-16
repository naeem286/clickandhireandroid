package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/14/2016.
 */
public class Bid {

    private int image;
    private String name, bid, date;
    public Bid(int image,String name, String bid, String date)
    {
        this.setImage(image);
        this.setName(name);
        this.setBid(bid);
        this.setDate(date);
    }

    public int getImage()
    {
        return image;
    }
    public void setImage(int image)
    {
        this.image=image;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getBid()
    {
        return bid;
    }
    public void setBid(String bid)
    {
        this.bid=bid;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date=date;
    }
}
